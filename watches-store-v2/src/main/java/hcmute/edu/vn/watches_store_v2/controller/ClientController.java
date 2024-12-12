package hcmute.edu.vn.watches_store_v2.controller;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.product.request.IdProductRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductReviewResponse;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.mapper.ReviewMapper;
import hcmute.edu.vn.watches_store_v2.service.component.CategoryService;
import hcmute.edu.vn.watches_store_v2.service.component.CouponService;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
import hcmute.edu.vn.watches_store_v2.service.component.ReviewService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("client")
@RequiredArgsConstructor
public class ClientController extends ControllerBase {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final CouponService couponService;
    private final ReviewService reviewService;

    @GetMapping("get-all-category")
    public ResponseEntity<?> getAllCategory() {
        try {
            return response(this.categoryService.getAllCategories(), HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get-all-product")
    public ResponseEntity<?> getAllProduct(
            @RequestParam(defaultValue = "none", required = false) String gender,
            @RequestParam(defaultValue = "none", required = false) String wireMaterial,
            @RequestParam(defaultValue = "none", required = false) String shape,
            @RequestParam(defaultValue = "none", required = false) String waterProof,
            @RequestParam(defaultValue = "none", required = false) String sortBy,
            @RequestParam(defaultValue = "none", required = false) String color,
            @RequestParam(defaultValue = "none", required = false) String q,
            @RequestParam(defaultValue = "none", required = false) String type,
            @RequestParam(defaultValue = "0", required = false) double minPrice,
            @RequestParam(defaultValue = "0", required = false) double maxPrice,
            @RequestParam(defaultValue = "1", required = false) int pageNum
    ) {
        try {
            return response(this.productService.getAllProduct(
                    gender,
                    wireMaterial,
                    shape,
                    waterProof,
                    sortBy,
                    color,
                    q,
                    type,
                    minPrice,
                    maxPrice,
                    pageNum
            ), HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-coupon")
    public ResponseEntity<?> getAllCoupon() {
        try {
            return response(this.couponService.getAllCoupons(), HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product")
    public ResponseEntity<?> getProductById(
            @RequestParam ObjectId productId) {
        try {
            ProductReviewResponse response = this.productService.getProductReview(productId);

            if (response == null) {
                return response(null, HttpStatus.NOT_FOUND);
            }

            return response(response, HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category")
    public  ResponseEntity<?> getCategoryById(
            @RequestParam ObjectId idCategory
    ) {
        try {
            List<ProductResponse> products = this.productService.getProductsByCategory(idCategory);
            if (products == null) {
                return response(null, HttpStatus.NOT_FOUND);
            }

            return response(products, HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-reviews-by-product")
    public ResponseEntity<?> getReviewsByProductId(@RequestParam ObjectId productId) {
        try {
            return response(this.reviewService.getReviewsByProduct(productId)
                            .stream()
                            .map(ReviewMapper::mapReviewResponse)
                            .collect(Collectors.toList())
                    , HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("get-full-product")
    public ResponseEntity<?> getFullProduct() {
        try {
            return response(this.productService.getAllProductResp().stream()
                    .filter(p -> p.getStateProduct().equals("selling") || p.getStateProduct().equals("outOfStock"))
                    , HttpStatus.OK);
        } catch (MongoException e) {
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
