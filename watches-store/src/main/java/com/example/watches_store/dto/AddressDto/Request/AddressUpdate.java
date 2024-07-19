package com.example.watches_store.dto.AddressDto.Request;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdate {
    private ObjectId id;
    
    private String province;
    
    private String district;

    private String ward;
}
