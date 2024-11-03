package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.dto.category.request.AssignCategoryRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.response.PageResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import org.bson.types.ObjectId;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProductResp();
    ProductResponse getProductById(ObjectId id);
    Product saveProduct(Product product);
    Product deleteProduct(ObjectId product);
    Product assignCategory(AssignCategoryRequest request);
    PageResponse getAllProduct(String gender, String wireMaterial
            , String shape, String waterProof, String sortBy, String color
            , String q
            , double minPrice, double maxPrice, int pageNum);

    PageResponse getAllProductAdmin(String gender, String wireMaterial
            , String shape, String waterProof, String sortBy, String color
            , String q
            , double minPrice, double maxPrice, int pageNum);

    List<ProductResponse> getProductsByCategory(ObjectId idCategory);
    List<ProductResponse> updateDiscountAllProduct(ObjectId idCategory, double discount);
    ProductResponse updateDiscount(ObjectId idProduct, double discount);
}