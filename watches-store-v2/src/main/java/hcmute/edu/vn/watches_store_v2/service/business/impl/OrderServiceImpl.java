package hcmute.edu.vn.watches_store_v2.service.business.impl;


import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.order.request.BuyNowRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.request.OrderRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderSuccessResponse;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.OrderLineDetail;
import hcmute.edu.vn.watches_store_v2.dto.product.Option;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.entity.OrderLine;
import hcmute.edu.vn.watches_store_v2.helper.payment_vnpay.PaymentService;
import hcmute.edu.vn.watches_store_v2.mapper.OrderMapper;
import hcmute.edu.vn.watches_store_v2.mapper.OrderLineMapper;
import hcmute.edu.vn.watches_store_v2.repository.OrderRepository;
import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import hcmute.edu.vn.watches_store_v2.service.component.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineService orderLineService;
    private final CouponService couponService;
    private final MailService mailService;
    private final ProductService productService;

    @Override
    public List<OrderResponse> getAllUserOrders(ObjectId userId) {
        try {

            return this.orderRepository.findAllByUserId(userId)
                    .stream()
                    .map(o -> OrderMapper.mapOrderResp(o))
                    .collect(Collectors.toList());
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't get user's order");
        }
    }

    @Override
    public OrderSuccessResponse createOrder(OrderRequest orderReq, ObjectId userId) {

        try {
            List<OrderLineResponse> itemResp = this.orderLineService.findAllByItemId(orderReq.getProductItem());

            ProfileOrder profileOrder = orderReq.getProfile();

            Coupon coupon = this.couponService.findCouponByCouponCode(orderReq.getCouponCode());

            Order order = OrderMapper.mapOrder(itemResp, profileOrder, orderReq, coupon);

            order.setUserId(userId);

            order.setItemsPrice(calculateItemPrice(order.getProducts()));

            double totalPrice = calculateTotalPrice(coupon, order);

            order.setTotalPrice(totalPrice);

            List<OrderLine> items = itemResp
                    .stream()
                    .map(i -> OrderLineMapper.mapOrderLineFromResp(i))
                    .collect(Collectors.toList());

            this.orderLineService.updateOrderItem(items);

            this.orderRepository.save(order);

//            this.mailService.orderSuccess(order);

            if (orderReq.getPaymentMethod().equals("vnpay"))
            {
                return OrderMapper.mapOrderSuccessResp(order, PaymentService.createPayment(order), false);
            }

            return OrderMapper.mapOrderSuccessResp(order, "http://localhost:5173", false);

        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't create order");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order cancleOrder(ObjectId order, ObjectId userId, String message) {

        Optional<Order> presentOrder = this.orderRepository.findById(order);

        if (presentOrder.isPresent() && presentOrder.get().getUserId().equals(userId)
                && !presentOrder.get().getState().equals("processing")) {

            presentOrder.get().setState("cancel");
            if (message == null)        presentOrder.get().setCancelMessage("Không có nhu cầu mua");
            else                        presentOrder.get().setCancelMessage(message);

            return this.orderRepository.save(presentOrder.get());
        }

        return null;
    }

    @Override
    public Order isPaid(ObjectId orderId) {
        Order order = this.orderRepository.findById(orderId).orElse(null);

        if (order != null) {
            order.setPaid(true);
            order.setPaidAt(new Date());

            this.orderRepository.save(order);
        }

        return null;
    }

    @Override
    public OrderSuccessResponse buyNow(BuyNowRequest buyNowRequest, ObjectId userId) throws UnsupportedEncodingException {
        ProductResponse product = this.productService.getProductById(buyNowRequest.getProductId());
        if (product == null)        return null;

        OrderLineResponse itemResponse = OrderLineMapper.mapNewResponse(product, buyNowRequest.getQuantity(), buyNowRequest.getOption());
        Coupon coupon = this.couponService.findCouponByCouponCode(buyNowRequest.getCouponCode());

        Order order = OrderMapper.mapNewOrder(List.of(itemResponse), buyNowRequest, coupon);

        order.setUserId(userId);

        order.setItemsPrice(calculateItemPrice(order.getProducts()));

        log.info("[OrderService] :: ItemPrice: {}", order.getItemsPrice());

        double totalPrice = calculateTotalPrice(coupon, order);

        order.setTotalPrice(totalPrice);

        log.info("[OrderService] :: TotalPrice: {}", order.getTotalPrice());

        this.orderRepository.save(order);

//        this.mailService.orderSuccess(order);

        if (buyNowRequest.getPaymentMethod().equals("vnpay"))
        {
            return OrderMapper.mapOrderSuccessResp(order, PaymentService.createPayment(order), true);
        }

        return OrderMapper.mapOrderSuccessResp(order, "http://localhost:5173", true);
    }

    @Override
    public OrderResponse isOrderDelivered(ObjectId orderId, ObjectId userId) {
        Order order = this.orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return null;
        }

        if (order.getPaymentMethod().equals("cash")) {
            order.setPaid(true);
            order.setPaidAt(new Date());
        }

        order.setDelivered(true);
        order.setDeliveredAt(new Date());
        order.setState("complete");

        this.orderRepository.save(order);
        return OrderMapper.mapOrderResp(order);
    }

    @Override
    public OrderResponse approvalOrder(ObjectId orderId) {

        Order order = this.orderRepository.findById(orderId).orElse(null);

        if (order != null) {
            order.setState("delivery");
            this.orderRepository.save(order);
        }

        return null;
    }

    @Override
    public OrderResponse declineOrder(ObjectId orderId) {
        Order order = this.orderRepository.findById(orderId).orElse(null);

        if (order != null) {
            order.setState("cancel");
            order.setCancelMessage("Admin decline");
            this.orderRepository.save(order);
        }

        return null;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return this.orderRepository.findAll().stream()
                .map(OrderMapper::mapOrderResp)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(ObjectId orderId) {
        try {
            return OrderMapper.mapOrderResp(this.orderRepository.findById(orderId).orElse(null));
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't get order");
        }
    }

    private double calculateItemPrice(List<OrderLineDetail> item) {
        double price = 0;

        for (OrderLineDetail i : item) {
                price += (i.getProduct().getOption().getValue().getPrice() - i.getProduct().getOption().getValue().getDiscount()) * i.getQuantity();
        }

        return price;
    }

    private double calculateTotalPrice(Coupon coupon, Order order) {
        double price = 0;
        if (coupon != null
                && coupon.getCreatedDate().before(new Date())
                && coupon.getExpiryDate().after(new Date())
                && coupon.getState().equals("active")
                && coupon.getTimes() > 0) {

            if (coupon.getProvince() != null
                    && (order.getUser().getAddress().getProvince().getValue() == coupon.getProvince().getValue()
                        || coupon.getProvince().getValue() == 0)) {
                coupon.setTimes(coupon.getTimes() - 1);
                price = order.getTotalPrice();
            }
            else if (coupon.getMinPrice() != 0 && coupon.getMinPrice() <= order.getItemsPrice()) {
                coupon.setTimes(coupon.getTimes() - 1);
                price = order.getItemsPrice() * (100 - coupon.getDiscount()) / 100 + order.getShippingPrice();
            }
            if (coupon.getTimes() == 0) {{
                coupon.setState("expired");
            }}
            this.couponService.saveCoupon(coupon);
        }

        if (price != 0)
            return price;
        else
            return order.getItemsPrice() + order.getShippingPrice();
    }
}
