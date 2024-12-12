package hcmute.edu.vn.watches_store_v2.controller.test;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.helper.momo_payment.MomoPaymentService;
import hcmute.edu.vn.watches_store_v2.repository.OrderRepository;
import hcmute.edu.vn.watches_store_v2.repository.ProductRepository;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController extends ControllerBase {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @GetMapping("/test")
    public ResponseEntity<?> test() {

        Order order = this.orderRepository.findById(new ObjectId("673ca832329c113856a5331f")).orElse(null);

        return response(MomoPaymentService.createPayment(order), HttpStatus.OK);
    }
}
