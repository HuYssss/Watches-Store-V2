package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.category.request.AssignCategoryRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.Option;
import hcmute.edu.vn.watches_store_v2.dto.product.response.PageResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductReviewResponse;
import hcmute.edu.vn.watches_store_v2.entity.Category;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import hcmute.edu.vn.watches_store_v2.entity.Review;
import hcmute.edu.vn.watches_store_v2.mapper.ProductMapper;
import hcmute.edu.vn.watches_store_v2.mapper.ReviewMapper;
import hcmute.edu.vn.watches_store_v2.repository.CategoryRepository;
import hcmute.edu.vn.watches_store_v2.repository.ProductRepository;
import hcmute.edu.vn.watches_store_v2.repository.ReviewRepository;
import hcmute.edu.vn.watches_store_v2.service.component.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

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

        try {

            for (Option o : product.getOption()) {
                o.getValue().setState("selling");
            }

            this.productRepository.save(product);
            return product;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save product");
        }
    }

    @Override
    public Product pauseProduct(ObjectId id) {
        Product product = this.productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        if (product.getStateProduct().contains("selling")) {
            product.setStateProduct("pause");
        } else if (product.getStateProduct().contains("pause")) {
            product.setStateProduct("selling");
        }

        for (Option o : product.getOption()) {
             if (product.getStateProduct().contains("pause")) {
                o.getValue().setState("pause");
            }
        }

        this.productRepository.save(product);

        return product;
    }

    @Override
    public Product deleteProduct(ObjectId productId) {
        try {
            Product currentProduct = this.productRepository.findById(productId).orElse(null);
            if (currentProduct == null) {
                return null;
            }

            currentProduct.setStateProduct("deleted");

            for (Option o : currentProduct.getOption()) {
                o.getValue().setState("deleted");
            }

            this.productRepository.save(currentProduct);

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
    public Product salingProduct(ObjectId productId) {
        Product product = this.productRepository.findById(productId).orElse(null);

        if (product == null)        return null;

        product.setStateProduct("selling");

        this.productRepository.save(product);

        return product;
    }

    @Override
    public Product pauseOption(ObjectId productId, String key) {
        Product product = this.productRepository.findById(productId).orElse(null);

        if (product == null)
            return null;

        for (Option o : product.getOption()) {
            if (o.getKey().equals(key)) {
                if (o.getValue().getState().equals("selling")) {
                    o.getValue().setState("pause");
                } else if (o.getValue().getState().equals(("pause"))) {
                    o.getValue().setState("selling");
                }
            }
        }

        product = checkStateProduct(product);

        this.productRepository.save(product);

        return product;
    }

    @Override
    public Product sellingOption(ObjectId productId, String key) {
        Product product = this.productRepository.findById(productId).orElse(null);

        if (product == null)
            return null;

        for (Option o : product.getOption()) {
            if (o.getKey().equals(key)) {
                o.getValue().setState("selling");
            }
        }

        this.productRepository.save(product);

        return product;
    }

    @Override
    public PageResponse getAllProduct(String gender, String wireMaterial, String shape, String waterProof
            , String sortBy, String color, String q, String type, double minPrice, double maxPrice, int pageNum) {

        log.info("[ProductServiceImpl] Starting all products");

        List<Product> products = this.productRepository.findAll();

        List<ProductResponse> allProducts = products
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
                                .anyMatch(c -> product.getOption().stream().anyMatch(option -> option.getKey().equals(color))))

                .filter(product -> (minPrice == 0 && maxPrice == 0)
                        || (minPrice > 0 && product.getOption().getFirst().getValue().getPrice() >= minPrice && (maxPrice == 0 || product.getOption().getFirst().getValue().getPrice() <= maxPrice))
                        || (maxPrice > 0 && product.getOption().getFirst().getValue().getPrice() <= maxPrice)
                )
                .filter(product -> waterProof.equals("none") || product.getWaterproof() == Integer.parseInt(waterProof))
                .filter(product -> type.equals("none") ||
                        Arrays.stream(type.split(","))
                                .map(String::trim)
                                .anyMatch(wm -> product.getType().toLowerCase(new Locale("vi", "VN")).contains(wm.toLowerCase(new Locale("vi", "VN")))))
                .filter(product -> !product.getStateProduct().equals("deleted"))
                .collect(Collectors.toList());

        if (!sortBy.equals("none")) {
            Comparator<ProductResponse> comparator = null;

            if (sortBy.equals("gia-giam-dan")) {
                comparator = Comparator.comparingDouble(ProductResponse::getPriceSafely).reversed();
            } else if (sortBy.equals("gia-tang-dan")) {
                comparator = Comparator.comparingDouble(ProductResponse::getPriceSafely);
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

        products.forEach(product -> {
            if (!pageResponse.getWireMaterial().contains(product.getWireMaterial())) {
                pageResponse.addTypeWireMaterial(product.getWireMaterial().trim());
            }

            if (!pageResponse.getShape().contains(product.getShape())) {
                pageResponse.addTypeShape(product.getShape());
            }

            if (!pageResponse.getWaterProof().contains(product.getWaterproof())) {
                pageResponse.addTypeWaterProof(product.getWaterproof());
            }

            if (!pageResponse.getType().contains(product.getType())) {
                pageResponse.addType(product.getType());
            }
        });

        return pageResponse;
    }

    @Override
    public PageResponse getAllProductAdmin(String gender, String wireMaterial
            , String shape, String waterProof, String sortBy, String color
            , String q, String type, String state
            , double minPrice, double maxPrice, int pageNum) {
        log.info("[ProductServiceImpl] Starting all products");

        List<Product> products = this.productRepository.findAll();

        List<ProductResponse> allProducts = products
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
                                .anyMatch(c -> product.getOption().stream().anyMatch(option -> option.getKey().equals(color))))

                .filter(product -> (minPrice == 0 && maxPrice == 0)
                        || (minPrice > 0 && product.getOption().getFirst().getValue().getPrice() >= minPrice && (maxPrice == 0 || product.getOption().getFirst().getValue().getPrice() <= maxPrice))
                        || (maxPrice > 0 && product.getOption().getFirst().getValue().getPrice() <= maxPrice)
                )
                .filter(product -> waterProof.equals("none") || product.getWaterproof() == Integer.parseInt(waterProof))
                .filter(product -> type.equals("none") ||
                        Arrays.stream(type.split(","))
                                .map(String::trim)
                                .anyMatch(wm -> product.getType().toLowerCase(new Locale("vi", "VN")).contains(wm.toLowerCase(new Locale("vi", "VN")))))
                .filter(product -> state.equals("none") || product.getStateProduct().contains(state))
                .collect(Collectors.toList());

        if (!sortBy.equals("none")) {
            Comparator<ProductResponse> comparator = null;

            if (sortBy.equals("gia-giam-dan")) {
                comparator = Comparator.comparingDouble(ProductResponse::getPriceSafely).reversed();
            } else if (sortBy.equals("gia-tang-dan")) {
                comparator = Comparator.comparingDouble(ProductResponse::getPriceSafely);
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

        products.forEach(product -> {
            if (!pageResponse.getWireMaterial().contains(product.getWireMaterial())) {
                pageResponse.addTypeWireMaterial(product.getWireMaterial().trim());
            }

            if (!pageResponse.getShape().contains(product.getShape())) {
                pageResponse.addTypeShape(product.getShape());
            }

            if (!pageResponse.getWaterProof().contains(product.getWaterproof())) {
                pageResponse.addTypeWaterProof(product.getWaterproof());
            }
        });

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

        products.forEach(product -> {
            product.getOption().forEach(option -> {
                option.getValue().setDiscount(discount);
            });
        });

        this.productRepository.saveAll(products);

        return products
                .stream()
                .map(ProductMapper::mapProductResp)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateDiscount(ObjectId idProduct, double discount, String key) {
        Product product = this.productRepository.findById(idProduct).orElse(null);
        if (product == null) {
            return null;
        }

        if (key.equals("none")) {
            product.getOption().forEach(option -> {
                option.getValue().setDiscount(discount);
            });
        } else {
            List<Option> options = product.getOption();

            List<Option> updatedOptions = options.stream().peek(option -> {
                if (option.getKey().equals(key)) {
                    option.getValue().setDiscount(discount);
                }
            }).collect(Collectors.toList());

            product.setOption(updatedOptions);
        }

        this.productRepository.save(product);

        return ProductMapper.mapProductResp(product);
    }

    @Override
    public ProductReviewResponse getProductReview(ObjectId idProduct) {
        Product product = this.productRepository.findById(idProduct).orElse(null);
        List<Review> reviews = this.reviewRepository.findByProductId(idProduct);

        if (product == null || reviews == null)     return null;

        product.setAccess(product.getAccess() + 1);
        this.productRepository.save(product);

        ProductReviewResponse response = ProductMapper.mapProductReview(product);

        response.setReviews(reviews.stream()
                .map(ReviewMapper::mapReviewResponse)
                .filter(r -> !r.isDelete())
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    public List<ProductResponse> getProductMultiple(List<ObjectId> idProduct) {

        List<ProductResponse> productResponses = this.productRepository.findAll()
                .stream()
                .map(ProductMapper::mapProductResp)
                .collect(Collectors.toList());

        List<ProductResponse> resp = new ArrayList<>();

        for (ProductResponse productResponse : productResponses) {
            for (ObjectId id : idProduct) {
                if (productResponse.getId().equals(id.toHexString())) {
                    resp.add(productResponse);
                }
            }
        }

        return resp;
    }

    public Product checkStateProduct(Product product)  {
        int count = 0;

        for (Option option : product.getOption()) {
            if (option.getValue().getState().equals("pause")) {
                count++;
            } else if (option.getValue().getState().equals("selling")) {
                product.setStateProduct("selling");
                break;
            }
        }

        if (count == product.getOption().size()) {
            product.setStateProduct("pause");
        }

        return product;
    }
}
