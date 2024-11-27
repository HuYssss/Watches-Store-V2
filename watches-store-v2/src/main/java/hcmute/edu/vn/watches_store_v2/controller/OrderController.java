package hcmute.edu.vn.watches_store_v2.controller;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.order.request.BuyNowRequest;
import hcmute.edu.vn.watches_store_v2.dto.order.request.CancelOrder;
import hcmute.edu.vn.watches_store_v2.dto.order.request.IdOrder;
import hcmute.edu.vn.watches_store_v2.dto.order.request.OrderRequest;
import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController extends ControllerBase {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        try {
            return response(this.orderService.createOrder(orderRequest, findIdByUsername(principal.getName()))
                    , HttpStatus.CREATED);
        } catch (MongoException e) {
            e.printStackTrace();
            return response("Can't create order"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllOrder(Principal principal) {
        try {
            return response(this.orderService.getAllUserOrders(findIdByUsername(principal.getName()))
                    , HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response("Can't find order"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
            @RequestBody CancelOrder cancelOrder,
            Principal principal)
    {
        try {
            return response(this.orderService.cancleOrder(cancelOrder.getId(), findIdByUsername(principal.getName()), cancelOrder.getMessage())
                    , HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response("Can't cancel order"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/buy-now")
    public ResponseEntity<?> buyNow(@RequestBody BuyNowRequest buyNowRequest, Principal principal) {
        try {
            return response(this.orderService.buyNow(buyNowRequest, findIdByUsername(principal.getName()))
                    , HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response("Can't buy now"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return response("Can't buy now"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/is-delivered")
    public ResponseEntity<?> isDelivered(@RequestBody IdOrder idOrder, Principal principal) {
        try {
            return response(this.orderService.isOrderDelivered(idOrder.getOrderId(), findIdByUsername(principal.getName())),
                    HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response("Server Error !!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/is-ordered")
    public ResponseEntity<?> isOrdered(@RequestParam ObjectId productId, Principal principal) {
        try {
            return response(this.orderService.isOrdered(productId, findIdByUsername(principal.getName())),
                    HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response("Server Error !!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
