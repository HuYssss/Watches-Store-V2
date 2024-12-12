package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.orderLine.response.OrderLineResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.Option;
import hcmute.edu.vn.watches_store_v2.dto.product.request.ProductRequest;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductOrder;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductReviewResponse;
import hcmute.edu.vn.watches_store_v2.entity.Product;
import org.bson.types.ObjectId;

import java.util.List;

public class ProductMapper {
    public static ProductResponse mapProductResp(Product product) {
        return new ProductResponse(
                product.getId().toHexString(),
                product.getProductName(),
                product.getImg(),
                product.getBrand(),
                product.getOrigin(),
                product.getWireMaterial(),
                product.getShellMaterial(),
                product.getStyle(),
                product.getFeature(),
                product.getShape(),
                product.getWeight(),
                product.getLength(),
                product.getWidth(),
                product.getHeight(),
                product.getGenderUser(),
                product.getDescription(),
                product.getOption(),
                product.getCategory().toHexString(),
                product.getWaterproof(),
                product.getType(),
                product.getStateProduct(),
                0,
                product.getAccess()
        );
    }

    public static Product mapProduct(ProductRequest productRequest) {
        return new Product(
                new ObjectId(),
                productRequest.getProductName(),
                productRequest.getImg(),
                productRequest.getBrand(),
                productRequest.getOrigin(),
                productRequest.getWireMaterial(),
                productRequest.getShellMaterial(),
                productRequest.getStyle(),
                productRequest.getFeature(),
                productRequest.getShape(),
                productRequest.getWeight(),
                productRequest.getLength(),
                productRequest.getWidth(),
                productRequest.getHeight(),
                productRequest.getGenderUser(),
                productRequest.getDescription(),
                productRequest.getOption(),
                (productRequest.getCategory() == null)
                        ? new ObjectId("66c710a6d6714b1d226daf5a")
                        : new ObjectId(productRequest.getCategory()),
                productRequest.getWaterproof(),
                productRequest.getType(),
                productRequest.getStateProduct(),
                0
        );
    }

    public static ProductOrder mapProductOrder(OrderLineResponse orderLineResponse) {
        return new ProductOrder(
                orderLineResponse.getProduct().getId(),
                orderLineResponse.getProduct().getProductName(),
                orderLineResponse.getProduct().getImg(),
                orderLineResponse.getProduct().getBrand(),
                orderLineResponse.getProduct().getOrigin(),
                orderLineResponse.getProduct().getWireMaterial(),
                orderLineResponse.getProduct().getShellMaterial(),
                orderLineResponse.getProduct().getStyle(),
                orderLineResponse.getProduct().getFeature(),
                orderLineResponse.getProduct().getShape(),
                orderLineResponse.getProduct().getWeight(),
                orderLineResponse.getProduct().getLength(),
                orderLineResponse.getProduct().getWidth(),
                orderLineResponse.getProduct().getHeight(),
                orderLineResponse.getProduct().getGenderUser(),
                orderLineResponse.getProduct().getDescription(),
                getOption(orderLineResponse.getProduct().getOption(), orderLineResponse.getOption()),
                orderLineResponse.getProduct().getCategory(),
                orderLineResponse.getProduct().getWaterproof(),
                orderLineResponse.getProduct().getType(),
                orderLineResponse.getProduct().getStateProduct()
        );
    }

    public static Option getOption(List<Option> options, String key) {
        return options.stream().filter(option -> option.getKey().equals(key)).findFirst().orElse(null);
    }

    public static ProductReviewResponse mapProductReview(Product product) {
        return new ProductReviewResponse(
                product.getId().toHexString(),
                product.getProductName(),
                product.getImg(),
                product.getBrand(),
                product.getOrigin(),
                product.getWireMaterial(),
                product.getShellMaterial(),
                product.getStyle(),
                product.getFeature(),
                product.getShape(),
                product.getWeight(),
                product.getLength(),
                product.getWidth(),
                product.getHeight(),
                product.getGenderUser(),
                product.getDescription(),
                product.getOption(),
                product.getCategory().toHexString(),
                product.getWaterproof(),
                product.getType(),
                product.getStateProduct(),
                product.getAccess(),
                null
        );
    }
}
