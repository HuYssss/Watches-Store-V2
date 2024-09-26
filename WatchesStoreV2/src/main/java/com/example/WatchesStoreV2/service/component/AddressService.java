package com.example.WatchesStoreV2.service.component;

import com.example.WatchesStoreV2.dto.address.request.AddressRequest;
import com.example.WatchesStoreV2.dto.address.response.AddressResponse;
import com.example.WatchesStoreV2.entity.Address;
import org.bson.types.ObjectId;

import java.util.List;

public interface AddressService {
    Address saveAddress(Address address);
    boolean deleteAddress(ObjectId addressId);
    List<AddressResponse> getUserAddresses(ObjectId userId);
    Address findAddress(ObjectId addressId);
}
