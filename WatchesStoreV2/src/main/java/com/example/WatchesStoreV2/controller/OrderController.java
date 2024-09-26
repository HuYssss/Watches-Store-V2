package com.example.WatchesStoreV2.controller;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.dto.order.request.OrderRequest;
import com.example.WatchesStoreV2.service.business.OrderService;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable("id") ObjectId id,
            @RequestParam(defaultValue = "Nhầm địa chỉ") String message,
            Principal principal)
    {
        try {
            return response(this.orderService.cancleOrder(id, findIdByUsername(principal.getName()), message)
                    , HttpStatus.OK);
        } catch (MongoException e) {
            e.printStackTrace();
            return response("Can't cancel order"
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
