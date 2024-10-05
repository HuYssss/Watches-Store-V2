package hcmute.edu.vn.watches_store_v2.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/payment-success")
    public void paymentSuccess(
            @RequestParam(value = "vnp_OrderInfo", defaultValue = "none") String vnp_OrderInfo,
            HttpServletResponse response) throws IOException
    {
        log.info("[Payment Controller]: Payment success");
        log.info("[Payment Controller]: vnp_OrderInfo = {}", vnp_OrderInfo);

        response.sendRedirect("http://localhost:5173");
    }
}
