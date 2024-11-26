package hcmute.edu.vn.watches_store_v2.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Option {

    private String key;

    private OptionValue value;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OptionValue {

        private double  price;

        private double  discount;

        private int quantity;

        private String  state;

        private String color;
        
    }
}
