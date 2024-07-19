package com.example.watches_store.service.component.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.watches_store.entity.Address;
import com.example.watches_store.repository.AddressRepository;
import com.example.watches_store.service.component.AddressService;
import com.mongodb.MongoException;

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
            throw new MongoException("Can't save address");
        }
    }

    @Override
    public boolean deleteAddress(ObjectId addressId) {
        try {
            this.addressRepository.deleteById(addressId);
            return true;
        } catch (MongoException e) {
            throw new MongoException("Can't delete address");
        }
    }

    @Override
    public List<Address> getUserAddresses(ObjectId userId) {
        List<Address> resp = new ArrayList<>();
        try {
            resp = this.addressRepository.findByUser(userId);
            return resp;
        } catch (MongoException e) {
            throw new MongoException("Can't find user's address");
        }
    }

    @Override
    public Address findAddress(ObjectId addressId) {
        try {
            Optional<Address> address = this.addressRepository.findById(addressId);
            return address.orElse(null);
        } catch (MongoException e) {
            throw new MongoException("Can't find address");
        }
    }
    
}
