package com.example.WatchesStoreV2.controller.admin;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.dto.product.request.ProductRequest;
import com.example.WatchesStoreV2.entity.Product;
import com.example.WatchesStoreV2.mapper.ProductMapper;
import com.example.WatchesStoreV2.service.component.ProductService;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product")
public class ProductController extends ControllerBase {

    private final ProductService productService;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {

        Product product = ProductMapper.mapProduct(productRequest);

        try {
            this.productService.saveProduct(product);
            return response("Created product !!!", HttpStatus.CREATED);

        } catch (MongoException e) {
            e.printStackTrace();
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable ObjectId id, @RequestBody ProductRequest productRequest) {

        Product product = ProductMapper.mapProduct(productRequest);
        product.setId(id);

        try {
            this.productService.saveProduct(product);
            return response("Updated product !!!", HttpStatus.OK);

        } catch (MongoException e) {
            e.printStackTrace();
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable ObjectId id) {

        try {
            this.productService.deleteProduct(id);
            return response("Deleted product !!!", HttpStatus.OK);

        } catch (MongoException e) {
            e.printStackTrace();
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/assign-category")
    public ResponseEntity<?> assignCategoryProduct(@RequestParam ObjectId productId, @RequestParam ObjectId categoryId) {
        try {
            Product product = this.productService.assignCategory(productId, categoryId);
            if (product == null)
                return response(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND);

            return response("Assigned category !!!", HttpStatus.OK);

        } catch (MongoException e) {
            e.printStackTrace();
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
