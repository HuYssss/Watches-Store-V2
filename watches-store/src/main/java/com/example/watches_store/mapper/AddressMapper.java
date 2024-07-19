package com.example.watches_store.mapper;

import org.bson.types.ObjectId;

import com.example.watches_store.dto.AddressDto.Request.AddressRequest;
import com.example.watches_store.dto.AddressDto.Response.AddressResponse;
import com.example.watches_store.entity.Address;

public class AddressMapper {
    public static Address reqAddressToAddress(AddressRequest addressRequest) {
        return new Address(
            new ObjectId(),
            addressRequest.getProvince(),
            addressRequest.getDistrict(),
            addressRequest.getWard(),
            null
        );
    }

    public static AddressResponse addressToAddressResp(Address address) {
        return new AddressResponse(
            address.getId().toHexString(),
            address.getProvince(),
            address.getDistrict(),
            address.getWard()
        );
    }
}
