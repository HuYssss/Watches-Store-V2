package hcmute.edu.vn.watches_store_v2.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.order.response.OrderResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.Option;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.mapper.OrderMapper;
import hcmute.edu.vn.watches_store_v2.repository.OrderRepository;
import hcmute.edu.vn.watches_store_v2.repository.ProductRepository;
import hcmute.edu.vn.watches_store_v2.service.business.OrderService;
import hcmute.edu.vn.watches_store_v2.service.component.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController extends ControllerBase {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/update")
    public ResponseEntity<String> update() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            product.setStateProduct("selling");
            for (Option option : product.getOption()) {
                option.getValue().setState("selling");
            }
        }
        productRepository.saveAll(products);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-order-realtime", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getAllRealtimeOrder() {
//        Order order = this.orderRepository.findById(new ObjectId("673a021b8099935668157de1")).orElse(null);

        List<OrderResponse> resp = this.orderRepository.findAll().stream().map(OrderMapper::mapOrderResp).collect(Collectors.toList());

        return Flux.interval(Duration.ofSeconds(5))
                .map(sequence -> {
                    String data = toJson(resp);
                    log.info("Send data: {}", data);
                    return data;
                })
                .doOnCancel(() -> log.info("Disconnection"));
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
