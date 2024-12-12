package hcmute.edu.vn.watches_store_v2.controller.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.order.request.CancelOrder;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.mapper.OrderMapper;
import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/order")
public class ManageOrderController extends ControllerBase {

    private final OrderService orderService;

    private final AtomicReference<List<OrderResponse>> sentOrders = new AtomicReference<>(new ArrayList<>());
    private boolean call_get_all = false;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/get-all-order")
    public ResponseEntity<?> getAllOrder(
            @RequestParam(defaultValue = "none") String state,
            @RequestParam(defaultValue = "none") String q) {
        List<OrderResponse> orderResponses = this.orderService.getAllOrders();
        if (orderResponses != null) {

            sentOrders.set(orderResponses);

            return response(orderResponses.stream()
                            .filter(o -> q.equals("none")
                                    || o.getUser().getName().toLowerCase(new Locale("vi", "VN")).contains(q.toLowerCase(new Locale("vi", "VN")))
                                    || o.getUser().getPhone().contains(q.trim())
                                    || o.getId().equals(q))
                            .filter(o -> state.equals("none") || o.getState().equals(state))
                    , HttpStatus.OK);
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
    public ResponseEntity<?> declineOrder(@RequestBody CancelOrder cancelOrder) {

        OrderResponse response = this.orderService.declineOrder(cancelOrder);

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

    @GetMapping(value = "/get-all-order-realtime", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getAllRealtimeOrder() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(sequence -> {
                    List<OrderResponse> newOrders = this.orderService.getAllOrders();
                    log.info("New orders: {}", newOrders.size());

                    List<OrderResponse> previousOrders = sentOrders.get();
                    log.info("Previous orders: {}", previousOrders.size());

                    List<OrderResponse> ordersToSend = newOrders.stream()
                            .filter(order -> !previousOrders.contains(order))
                            .toList();

                    if (call_get_all) {
                        call_get_all = false;
                        sentOrders.set(newOrders);
                    }

                    String data = toJson(ordersToSend);
                    log.info("Send data: {}", data);
                    return data;
                })
                .doOnCancel(() -> log.info("Disconnection"));
    }

    @GetMapping("/statistics-admin")
    public ResponseEntity<?> getOrderStatistics() {
        try {
            return response(this.orderService.statisticAdmin(), HttpStatus.OK);
        } catch (Exception e) {
            return response(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }
}
