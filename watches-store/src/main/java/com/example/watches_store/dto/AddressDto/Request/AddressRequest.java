package com.example.watches_store.dto.AddressDto.Request;

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
