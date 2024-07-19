package com.example.watches_store.dto.AddressDto.Response;

import org.bson.types.ObjectId;

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
