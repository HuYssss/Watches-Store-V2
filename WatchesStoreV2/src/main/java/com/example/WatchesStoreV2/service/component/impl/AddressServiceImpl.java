package com.example.WatchesStoreV2.service.component.impl;

import com.example.WatchesStoreV2.dto.address.response.AddressResponse;
import com.example.WatchesStoreV2.entity.Address;
import com.example.WatchesStoreV2.mapper.AddressMapper;
import com.example.WatchesStoreV2.repository.AddressRepository;
import com.example.WatchesStoreV2.service.component.AddressService;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address saveAddress(Address address) {
        try {
            this.addressRepository.save(address);
            return address;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save address");
        }
    }

    @Override
    public boolean deleteAddress(ObjectId addressId) {
        try {
            this.addressRepository.deleteById(addressId);
            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't delete address");
        }
    }

    @Override
    public List<AddressResponse> getUserAddresses(ObjectId userId) {
        try {
            List<Address> addresses = this.addressRepository.findByUser(userId);
            return addresses.stream()
                    .map(a -> AddressMapper.addressToAddressResp(a))
                    .collect(Collectors.toList());
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find user's address");
        }
    }

    @Override
    public Address findAddress(ObjectId addressId) {
        try {
            Optional<Address> address = this.addressRepository.findById(addressId);
            return address.orElse(null);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find address");
        }
    }
}
