package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.product.request.ProductRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import org.bson.types.ObjectId;

public class ProductMapper {
    public static ProductResponse mapProductResp(Product product) {
        return new ProductResponse(
                product.getId().toHexString(),
                product.getProductName(),
                product.getImg(),
                product.getPrice(),
                product.getBrand(),
                product.getOrigin(),
                product.getWireMaterial(),
                product.getShellMaterial(),
                product.getStyle(),
                product.getFeature(),
                product.getShape(),
                product.getCondition(),
                product.getWeight(),
                product.getLength(),
                product.getWidth(),
                product.getHeight(),
                product.getGenderUser(),
                product.getDescription(),
                product.getColor(),
                product.getDiscount(),
                product.getCategory().toHexString(),
                product.getAmount(),
                product.getWaterproof(),
                product.getState()
        );
    }

    public static Product mapProduct(ProductRequest productRequest) {
        return new Product(
                new ObjectId(),
                productRequest.getProductName(),
                productRequest.getImg(),
                productRequest.getPrice(),
                productRequest.getBrand(),
                productRequest.getOrigin(),
                productRequest.getWireMaterial(),
                productRequest.getShellMaterial(),
                productRequest.getStyle(),
                productRequest.getFeature(),
                productRequest.getShape(),
                productRequest.getCondition(),
                productRequest.getWeight(),
                productRequest.getLength(),
                productRequest.getWidth(),
                productRequest.getHeight(),
                productRequest.getGenderUser(),
                productRequest.getDescription(),
                productRequest.getColor(),
                0,
                (productRequest.getCategory() == null)
                        ? new ObjectId("66c710a6d6714b1d226daf5a")
                        : new ObjectId(productRequest.getCategory()),
                productRequest.getAmount(),
                productRequest.getWaterproof(),
                productRequest.getState()
        );
    }
}
