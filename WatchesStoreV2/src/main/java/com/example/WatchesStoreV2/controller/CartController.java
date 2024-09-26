package com.example.WatchesStoreV2.controller;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.dto.productItem.request.ProductItemRequest;
import com.example.WatchesStoreV2.dto.productItem.request.ProductItemUpdateRequest;
import com.example.WatchesStoreV2.dto.productItem.response.ProductItemResponse;
import com.example.WatchesStoreV2.service.business.CartService;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController extends ControllerBase {
    private final CartService cartService;

    @GetMapping("/get-cart-user")
    public ResponseEntity<?> getCartUser(Principal principal) {
        try {
            return response(this.cartService.getCart(findIdByUsername(principal.getName())), HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-product-to-cart")
    public ResponseEntity<?> addProductToCart(@RequestBody ProductItemRequest productItemRequest, Principal principal) {
        try {
            return response(
                    this.cartService.addProductToCart(findIdByUsername(principal.getName()), productItemRequest)
                    , HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-cart")
    public ResponseEntity<?> updateCart(@RequestBody List<ProductItemUpdateRequest> updateRequests, Principal principal) {
        try {
            return response(
                    this.cartService.updateCart(findIdByUsername(principal.getName()), updateRequests)
                    , HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove-item/{id}")
    public ResponseEntity<?> removeItem(@PathVariable ObjectId id, Principal principal) {
        try {
            return response(
                    this.cartService.deleteProductFromCart(findIdByUsername(principal.getName()), id)
                    , HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
