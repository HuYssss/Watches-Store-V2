package com.example.watches_store.service.component;

import java.util.List;

import org.bson.types.ObjectId;

import com.example.watches_store.entity.Address;

public interface AddressService {
    Address saveAddress(Address address);
    boolean deleteAddress(ObjectId addressId);
    List<Address> getUserAddresses(ObjectId userId);
    Address findAddress(ObjectId addressId);
}
