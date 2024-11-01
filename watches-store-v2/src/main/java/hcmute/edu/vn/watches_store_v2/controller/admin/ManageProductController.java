package hcmute.edu.vn.watches_store_v2.controller.admin;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.product.request.ProductRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.request.UpdateDiscount;
import hcmute.edu.vn.watches_store_v2.dto.product.request.UpdateDiscountProducts;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.mapper.ProductMapper;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
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
@RequestMapping("api/product")
public class ManageProductController extends ControllerBase {
    private final ProductService productService;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest product) {
        if (product.getState() == null)         product.setState("waiting");
        return response(this.productService.saveProduct(ProductMapper.mapProduct(product)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) {
        return response(this.productService.saveProduct(product), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/update-discount-category")
    public ResponseEntity<?> updateDiscountCategory(@RequestBody UpdateDiscountProducts req) {
        List<ProductResponse> resp = this.productService.updateDiscountAllProduct(req.getCategoryId(), req.getDiscount());

        if (resp == null)
            return response("Not found category", HttpStatus.NOT_FOUND);

        return response(resp, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/update-discount-product")
    public ResponseEntity<?> updateDiscountProduct(@RequestBody UpdateDiscount req) {
        ProductResponse resp = this.productService.updateDiscount(req.getProductId(), req.getDiscount());

        if (resp == null)
            return response("Not found product", HttpStatus.NOT_FOUND);

        return response(resp, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam ObjectId productId) {
        Product product = this.productService.deleteProduct(productId);

        if (product == null)
            return response(null, HttpStatus.NOT_FOUND);

        return response(null, HttpStatus.OK);
    }
}
