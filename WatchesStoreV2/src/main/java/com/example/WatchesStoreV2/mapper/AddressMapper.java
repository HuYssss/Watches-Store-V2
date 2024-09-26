package com.example.WatchesStoreV2.mapper;

import com.example.WatchesStoreV2.dto.address.request.AddressRequest;
import com.example.WatchesStoreV2.dto.address.response.AddressResponse;
import com.example.WatchesStoreV2.entity.Address;
import org.bson.types.ObjectId;

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
