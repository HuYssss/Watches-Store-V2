package hcmute.edu.vn.watches_store_v2.controller.test;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.repository.OrderRepository;
import hcmute.edu.vn.watches_store_v2.repository.ProductRepository;
import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import hcmute.edu.vn.watches_store_v2.service.component.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController extends ControllerBase {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final MailService mailService;

    @GetMapping("/product")
    public ResponseEntity<?> getAllProducts() {
        return response(
                this.productRepository.findAll(),
                HttpStatus.OK
        );
    }

    @GetMapping("/order-success")
    public ResponseEntity<?> getOrderSuccess(@RequestParam String id) {

        Order order = this.orderRepository.findById(new ObjectId(id)).orElse(null);

        this.mailService.orderSuccess(order);

        return response(null, HttpStatus.OK);
    }
}
