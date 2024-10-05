package hcmute.edu.vn.watches_store_v2.service.component.impl;


import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.category.response.CategoryResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.entity.Category;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.mapper.CategoryMapper;
import hcmute.edu.vn.watches_store_v2.repository.CategoryRepository;
import hcmute.edu.vn.watches_store_v2.service.component.CategoryService;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @Override
    public Category findCategoryById(ObjectId id) {
        try {
            return this.categoryRepository.findById(id).orElse(null);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find category by id: " + id.toHexString());
        }
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        try {
            List<Category> categories = this.categoryRepository.findAll();
            List<CategoryResponse> categoriesResponse = categories.stream().map(c -> CategoryMapper.mapCategoryResp(c))
                    .collect(Collectors.toList());

            List<ProductResponse> products = this.productService.getAllProductResp();

            for (CategoryResponse category : categoriesResponse) {
                for (ProductResponse product : products) {
                    if (product.getCategory().equals(category.getId()))
                        category.setProduct(product);
                }
            }

            return categoriesResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MongoException("Can't get all categories");
        }
    }

    @Override
    public Category saveCategory(Category category) {
        try {
            this.categoryRepository.save(category);
            return category;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MongoException("Can't save category");
        }
    }

    @Override
    public Category deleteCategory(ObjectId categoryId) {
        try {
            Category category = this.categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                this.categoryRepository.delete(category);
                return category;
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MongoException("Can't delete category");
        }
    }
}
