package com.example.WatchesStoreV2.dto.address.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private String id;

    private String province;

    private String district;

    private String ward;
}
