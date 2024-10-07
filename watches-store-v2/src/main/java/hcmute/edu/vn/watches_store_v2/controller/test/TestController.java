package hcmute.edu.vn.watches_store_v2.controller.test;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/product")
    public ResponseEntity<?> getAllProducts() {
        return response(
                this.productRepository.findAll(),
                HttpStatus.OK
        );
    }
}
