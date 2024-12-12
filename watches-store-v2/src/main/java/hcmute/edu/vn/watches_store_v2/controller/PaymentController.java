package hcmute.edu.vn.watches_store_v2.controller;

import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class PaymentController {

    private final OrderService orderService;

    @GetMapping("/payment-success")
    public void paymentSuccess(
            @RequestParam(value = "vnp_OrderInfo", defaultValue = "none") String vnp_OrderInfo,
            @RequestParam(value = "orderInfo", defaultValue = "none") String orderInfo,
            HttpServletResponse response) throws IOException
    {
        log.info("[Payment Controller]: Payment success");
        log.info("[Payment Controller]: vnp_OrderInfo = {}", vnp_OrderInfo);
        log.info("[Payment Controller]: orderInfo = {}", orderInfo);

        ObjectId orderId = null;
        if (!vnp_OrderInfo.equals("none")) {
            orderId = new ObjectId(vnp_OrderInfo);
        }

        if (!orderInfo.equals("none")) {
            orderId = new ObjectId(orderInfo);
        }

        if (orderId != null) {
            this.orderService.isPaid(orderId);
        }
        
        response.sendRedirect("http://localhost:5173");
    }
}
