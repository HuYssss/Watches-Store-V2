package hcmute.edu.vn.watches_store_v2.service.business.impl;


import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.order.request.OrderRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.dto.productItem.response.ProductItemResponse;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import hcmute.edu.vn.watches_store_v2.entity.Coupon;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.entity.ProductItem;
import hcmute.edu.vn.watches_store_v2.helper.payment_vnpay.PaymentService;
import hcmute.edu.vn.watches_store_v2.mapper.OrderMapper;
import hcmute.edu.vn.watches_store_v2.mapper.ProductItemMapper;
import hcmute.edu.vn.watches_store_v2.repository.OrderRepository;
import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import hcmute.edu.vn.watches_store_v2.service.component.CouponService;
import hcmute.edu.vn.watches_store_v2.service.component.ProductItemService;
import hcmute.edu.vn.watches_store_v2.service.component.UserService;
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
    private final ProductItemService productItemService;
    private final UserService userService;
    private final CouponService couponService;

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
    public String createOrder(OrderRequest orderReq, ObjectId userId) {

        try {
            List<ProductItemResponse> itemResp = this.productItemService.findAllByItemId(orderReq.getProductItem());

            ProfileOrder profileOrder = orderReq.getProfile();

            Coupon coupon = this.couponService.findCouponByCouponCode(orderReq.getCouponCode());

            Order order = OrderMapper.mapOrder(itemResp, profileOrder, orderReq, coupon);

            order.setUserId(userId);

            order.setItemsPrice(calculateItemPrice(itemResp));

            double totalPrice = calculateTotalPrice(coupon, order);

            order.setTotalPrice(totalPrice);

            List<ProductItem> items = itemResp
                    .stream()
                    .map(i -> ProductItemMapper.mapProductItemFromResp(i))
                    .collect(Collectors.toList());

            this.productItemService.updateOrderItem(items);

            this.orderRepository.save(order);

            if (orderReq.getPaymentMethod().equals("vnpay"))
            {
                return PaymentService.createPayment(order);
            }

            return "Created Order";

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

        if (presentOrder.isPresent() && presentOrder.get().getUserId().equals(userId)) {

            presentOrder.get().setState("cancel");
            presentOrder.get().setCancelMessage(message);

            return this.orderRepository.save(presentOrder.get());
        }

        return null;
    }

    private double calculateItemPrice(List<ProductItemResponse> itemResp) {
        double price = 0;

        for (ProductItemResponse productItemResponse : itemResp) {
            price += (productItemResponse.getProduct().getPrice() - productItemResponse.getProduct().getDiscount()) * productItemResponse.getQuantity();
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

            if (!coupon.getProvince().equals("none") && order.getUser().getAddress().getProvince().getLabel().contains(coupon.getProvince())) {
                coupon.setTimes(coupon.getTimes() - 1);
                price = order.getTotalPrice();
            }
            else if (coupon.getMinPrice() != 0 && coupon.getMinPrice() <= order.getItemsPrice()) {
                coupon.setTimes(coupon.getTimes() - 1);
                price = order.getItemsPrice() * (1 - coupon.getDiscount()) + order.getShippingPrice();
            }

            this.couponService.saveCoupon(coupon);
        }

        if (price != 0)
            return price;
        else
            return order.getItemsPrice() + order.getShippingPrice();
    }
}
