package hcmute.edu.vn.watches_store_v2.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private Province province;
    private District district;
    private Ward ward;
    private String fullAddress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Province {
        private String label;
        private int value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class District {
        private String label;
        private int value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ward {
        private String label;
        private String value;
    }
}