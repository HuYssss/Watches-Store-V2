package com.example.WatchesStoreV2.controller.admin;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.entity.Category;
import com.example.WatchesStoreV2.service.component.CategoryService;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryController extends ControllerBase {

    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestParam String categoryName) {
        Category category = new Category(new ObjectId(), categoryName);

        try {
            this.categoryService.saveCategory(category);
            return response(category, HttpStatus.CREATED);

        } catch (MongoException e) {
            e.printStackTrace();
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable ObjectId id) {
        try {
            Category category = this.categoryService.deleteCategory(id);

            if (category == null) {
                return response("Incorrect Id !!!", HttpStatus.NOT_FOUND);
            }

            return response(category, HttpStatus.OK);

        } catch (MongoException e) {
            e.printStackTrace();
            return response(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
