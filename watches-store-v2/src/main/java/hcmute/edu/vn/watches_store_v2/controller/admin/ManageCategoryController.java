package hcmute.edu.vn.watches_store_v2.controller.admin;

import hcmute.edu.vn.watches_store_v2.base.ControllerBase;
import hcmute.edu.vn.watches_store_v2.dto.category.request.AssignCategoryRequest;
import hcmute.edu.vn.watches_store_v2.dto.category.request.CategoryRequest;
import hcmute.edu.vn.watches_store_v2.entity.Category;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.mapper.CategoryMapper;
import hcmute.edu.vn.watches_store_v2.service.component.CategoryService;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/category")
public class ManageCategoryController extends ControllerBase {

    private final CategoryService categoryService;
    private final ProductService productService;

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("get-all-category")
    public ResponseEntity<?> getAllCategory() {
        return response(this.categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryReq) {
        Category category = CategoryMapper.mapCategory(categoryReq);

        return response(this.categoryService.saveCategory(category), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        return response(this.categoryService.saveCategory(category), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam ObjectId categoryId) {
        Category category = this.categoryService.findCategoryById(categoryId);

        if (category == null) {
            return response(null, HttpStatus.NOT_FOUND);
        }

        return response(this.categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @PatchMapping("/assign-category")
    public ResponseEntity<?> assignCategory(@RequestBody AssignCategoryRequest request) {
        Product product = this.productService.assignCategory(request);

        if (product == null) {
            return response("Product not found", HttpStatus.NOT_FOUND);
        }

        return response(this.productService.assignCategory(request), HttpStatus.OK);
    }
}
