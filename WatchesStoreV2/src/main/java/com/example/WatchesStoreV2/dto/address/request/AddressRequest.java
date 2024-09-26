package com.example.WatchesStoreV2.dto.address.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String province;

    private String district;

    private String ward;
}
