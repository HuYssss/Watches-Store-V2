package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.category.request.AssignCategoryRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.response.PageResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.entity.Category;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.mapper.ProductMapper;
import hcmute.edu.vn.watches_store_v2.repository.CategoryRepository;
import hcmute.edu.vn.watches_store_v2.repository.ProductRepository;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductResponse> getAllProductResp() {
        try {
            return this.productRepository.findAll()
                    .stream()
                    .map(p -> ProductMapper.mapProductResp(p))
                    .collect(Collectors.toList());
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't get all products");
        }
    }

    @Override
    public ProductResponse getProductById(ObjectId id) {
        try {
            Product product = this.productRepository.findById(id).orElse(null);

            if (product != null)
                return ProductMapper.mapProductResp(product);

            return null;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't get product by id");
        }
    }

    @Override
    public Product saveProduct(Product product) {
        if (product.getId() != null) {
            ObjectId id = new ObjectId();
            product.setId(id);
        }

        try {
            this.productRepository.save(product);
            return product;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save product");
        }
    }

    @Override
    public Product deleteProduct(ObjectId productId) {
        try {
            Product currentProduct = this.productRepository.findById(productId).orElse(null);
            if (currentProduct == null) {
                return null;
            }

            this.productRepository.delete(currentProduct);
            return currentProduct;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete product");
        }
    }

    @Override
    public Product assignCategory(AssignCategoryRequest request) {
        Category currentCategory = this.categoryRepository.findById(request.getCategoryId()).orElse(null);
        Product product = this.productRepository.findById(request.getProductId()).orElse(null);

        if (product == null) {
            return null;
        }

        if (request.getCategoryId() == null || currentCategory == null)
            request.setCategoryId(new ObjectId("66c710a6d6714b1d226daf5a"));

        product.setCategory(request.getCategoryId());

        try {
            saveProduct(product);
            return product;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save product");
        }
    }

    @Override
    public PageResponse getAllProduct(String gender, String wireMaterial, String shape, String waterProof
            , String sortBy, String color, String q, double minPrice, double maxPrice, int pageNum) {

        log.info("[ProductServiceImpl] Starting all products");

        List<ProductResponse> allProducts = this.productRepository.findAll()
                .stream()
                .map(ProductMapper::mapProductResp)
                .filter(product -> gender.equals("none") || product.getGenderUser().toLowerCase(new Locale("vi", "VN")).contains(gender.toLowerCase(new Locale("vi", "VN"))))

                .filter(product -> wireMaterial.equals("none") ||
                        Arrays.stream(wireMaterial.split(","))
                                .map(String::trim)
                                .anyMatch(wm -> product.getWireMaterial().toLowerCase(new Locale("vi", "VN")).contains(wm.toLowerCase(new Locale("vi", "VN")))))

                .filter(product -> shape.equals("none") ||
                        Arrays.stream(shape.split(","))
                                .map(String::trim)
                                .anyMatch(s -> product.getShape().toLowerCase(new Locale("vi", "VN")).contains(shape.toLowerCase(new Locale("vi", "VN")))))

                .filter(product -> q.equals("none") || product.getProductName().toLowerCase(new Locale("vi", "VN")).contains(q.toLowerCase(new Locale("vi", "VN"))))

                .filter(product -> color.equals("none") ||
                        Arrays.stream(color.split(","))
                                .map(String::trim)
                                .anyMatch(c -> product.getColor().toLowerCase(new Locale("vi", "VN")).contains(color.toLowerCase(new Locale("vi", "VN")))))

                .filter(product -> (minPrice == 0 && maxPrice == 0)
                        || (minPrice > 0 && product.getPrice() >= minPrice && (maxPrice == 0 || product.getPrice() <= maxPrice))
                        || (maxPrice > 0 && product.getPrice() <= maxPrice)
                )
                .filter(product -> waterProof.equals("none") || product.getWaterproof() == Integer.parseInt(waterProof))
                .collect(Collectors.toList());

        if (!sortBy.equals("none")) {
            Comparator<ProductResponse> comparator = null;

            if (sortBy.equals("gia-giam-dan")) {
                comparator = Comparator.comparingDouble(ProductResponse::getPrice).reversed();
            } else if (sortBy.equals("gia-tang-dan")) {
                comparator = Comparator.comparingDouble(ProductResponse::getPrice);
            } else if (sortBy.equals("a-z")) {
                comparator = Comparator.comparing(ProductResponse::getProductName);
            } else if (sortBy.equals("z-a")) {
                comparator = Comparator.comparing(ProductResponse::getProductName).reversed();
            }

            if (comparator != null) {
                allProducts = allProducts.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
        }

        int itemPerPage = 12;
        int startIndex = (pageNum - 1) * itemPerPage;
        int totalItems = allProducts.size();
        int totalPages = (totalItems % 12 > 0) ? totalItems / 12 + 1 : totalItems / 12;

        PageResponse pageResponse = new PageResponse();
        pageResponse.setTotalProducts(totalItems);

        if (startIndex > allProducts.size()) {
            return null;
        }

        if (allProducts.size() - startIndex < 12) {
            itemPerPage = allProducts.size() - startIndex;
        }

        if (pageNum > 0 && startIndex < allProducts.size()) {
            allProducts = allProducts.subList(startIndex, Math.min(startIndex + itemPerPage, allProducts.size()));
        }

        pageResponse.setProductResponses(allProducts);
        pageResponse.setTotalPages(totalPages);
        return pageResponse;
    }

    @Override
    public List<ProductResponse> getProductsByCategory(ObjectId idCategory) {
        try {
            List<Product> products = this.productRepository.findByCategory(idCategory);

            if (products == null)
                return null;

            return products.stream().map(ProductMapper::mapProductResp).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MongoException("Can't get products by id: " + idCategory);
        }
    }

    @Override
    public List<ProductResponse> updateDiscountAllProduct(ObjectId idCategory, double discount) {
        List<Product> products = this.productRepository.findByCategory(idCategory);

        if (products == null) {
            return null;
        }

        this.productRepository.saveAll(products);

        return products
                .stream()
                .peek(product -> product.setDiscount(discount * product.getPrice()))
                .map(ProductMapper::mapProductResp)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateDiscount(ObjectId idProduct, double discount) {
        Product product = this.productRepository.findById(idProduct).orElse(null);
        if (product == null) {
            return null;
        }

        product.setDiscount(discount * product.getPrice());
        this.productRepository.save(product);

        return ProductMapper.mapProductResp(product);
    }
}
