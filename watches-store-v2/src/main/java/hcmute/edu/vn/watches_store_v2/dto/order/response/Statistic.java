package hcmute.edu.vn.watches_store_v2.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {

    private Status status;
    private List<Price> prices;

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
}
