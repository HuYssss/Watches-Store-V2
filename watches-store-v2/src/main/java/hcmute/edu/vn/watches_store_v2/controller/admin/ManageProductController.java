package hcmute.edu.vn.watches_store_v2.controller.admin;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.product.request.IdProductRequest;
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
    @GetMapping("/get-all-product")
    public ResponseEntity<?> getAllProduct(
            @RequestParam(defaultValue = "none", required = false) String gender,
            @RequestParam(defaultValue = "none", required = false) String wireMaterial,
            @RequestParam(defaultValue = "none", required = false) String shape,
            @RequestParam(defaultValue = "none", required = false) String waterProof,
            @RequestParam(defaultValue = "topSelling", required = false) String sortBy,
            @RequestParam(defaultValue = "none", required = false) String color,
            @RequestParam(defaultValue = "none", required = false) String q,
            @RequestParam(defaultValue = "none", required = false) String type,
            @RequestParam(defaultValue = "none", required = false) String state,
            @RequestParam(defaultValue = "0", required = false) double minPrice,
            @RequestParam(defaultValue = "0", required = false) double maxPrice,
            @RequestParam(defaultValue = "1", required = false) int pageNum
    ) {
        try {
            return response(this.productService.getAllProductAdmin(
                    gender,
                    wireMaterial,
                    shape,
                    waterProof,
                    sortBy,
                    color,
                    q,
                    type,
                    state,
                    minPrice,
                    maxPrice,
                    pageNum
            ), HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest product) {
        if (product.getStateProduct() == null)         product.setStateProduct("selling");
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
    public ResponseEntity<?> updateDiscountProduct(@RequestBody UpdateDiscount req, @RequestParam(defaultValue = "none") String key) {
        ProductResponse resp = this.productService.updateDiscount(req.getProductId(), req.getDiscount(), key);

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

        return response(ProductMapper.mapProductResp(product), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("/selling-product")
    public ResponseEntity<?> sellingProduct(@RequestParam ObjectId productId) {
        Product product = this.productService.salingProduct(productId);

        if (product == null)
            return response(null, HttpStatus.NOT_FOUND);

        return response(ProductMapper.mapProductResp(product), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/pause-product")
    public ResponseEntity<?> pauseProduct(@RequestParam ObjectId productId) {
        Product product = this.productService.pauseProduct(productId);

        if (product == null)
            return response(null, HttpStatus.NOT_FOUND);

        return response(ProductMapper.mapProductResp(product), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/pause-option")
    public ResponseEntity<?> pauseOptionProduct(@RequestParam ObjectId productId,
                                                @RequestParam(defaultValue = "none") String key) {

        if (key.equals("none"))
            return response("Key option cannot be null", HttpStatus.BAD_REQUEST);

        Product product = this.productService.pauseOption(productId, key);

        if (product == null) {
            return response("Product not found", HttpStatus.NOT_FOUND);
        }

        return response(ProductMapper.mapProductResp(product), HttpStatus.OK);
    }
}
