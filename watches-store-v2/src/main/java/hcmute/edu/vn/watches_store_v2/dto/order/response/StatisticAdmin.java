package hcmute.edu.vn.watches_store_v2.dto.order.response;

import hcmute.edu.vn.watches_store_v2.dto.product.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticAdmin {
    private StatisticAdmin.Status status;
    private List<StatisticAdmin.Price> prices;
    private List<ProductResponse> top5Selling;
    private List<StatisticAdmin.Gender> genders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status {
        private int processing = 0;
        private int delivery = 0;
        private int complete = 0;
        private int cancel = 0;

        public void increaseProcessing() {
            this.processing++;
        }

        public void increaseDelivery() {
            this.delivery++;
        }

        public void increaseComplete() {
            this.complete++;
        }

        public void increaseCancel() {
            this.cancel++;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Price {
        private int month;
        private double price;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Gender {
        private String gender;
        private int month;
        private int quantity;
    }
}
