package hcmute.edu.vn.watches_store_v2.controller.admin;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/order")
public class ManageOrderController extends ControllerBase {

    private final OrderService orderService;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/get-all-order")
    public ResponseEntity<?> getAllOrder() {
        List<OrderResponse> orderResponses = this.orderService.getAllOrders();
        if (orderResponses != null) {
            return response(orderResponses, HttpStatus.OK);
        }

        return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/approval-oder")
    public ResponseEntity<?> approveOrder(@RequestParam ObjectId orderId) {
        OrderResponse response = this.orderService.approvalOrder(orderId);

        if (response != null) {
            return response(response, HttpStatus.OK);
        }

        return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/decline-oder")
    public ResponseEntity<?> declineOrder(@RequestParam ObjectId orderId) {
        OrderResponse response = this.orderService.declineOrder(orderId);

        if (response != null) {
            return response(response, HttpStatus.OK);
        }

        return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("get-by-id")
    public ResponseEntity<?> getOrderById(@RequestParam ObjectId orderId) {
        try {
            OrderResponse response = this.orderService.getOrderById(orderId);
            if (response != null) {
                return response(response, HttpStatus.OK);
            } else
                return response(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
