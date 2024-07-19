package com.example.watches_store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.watches_store.base.ControllerBase;
import com.example.watches_store.dto.AddressDto.Request.AddressRequest;
import com.example.watches_store.dto.AddressDto.Request.AddressUpdate;
import com.example.watches_store.dto.AddressDto.Response.AddressResponse;
import com.example.watches_store.entity.Address;
import com.example.watches_store.mapper.AddressMapper;
import com.example.watches_store.service.component.AddressService;
import com.mongodb.MongoException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/address")
public class AddressController extends ControllerBase {

    @Autowired
    private AddressService addressService;

    @GetMapping("")
    public ResponseEntity<?> getUserAddresses(Principal principal) {
        List<Address> respAddress = new ArrayList<>();

        try {
            respAddress = this.addressService.getUserAddresses(findIdByUsername(principal.getName()));
            List<AddressResponse> responses = respAddress.stream()
                                                .map(a -> AddressMapper.addressToAddressResp(a))
                                                .collect(Collectors.toList());
            return response(responses, HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewAddress(@RequestBody AddressRequest addressRequest, Principal principal) {
        
        if (ObjectUtils.isEmpty(addressRequest) 
                || ObjectUtils.isEmpty(addressRequest.getProvince())
                || ObjectUtils.isEmpty(addressRequest.getDistrict())
                || ObjectUtils.isEmpty(addressRequest.getWard()))
            return response("No param", HttpStatus.BAD_REQUEST);

        Address address = AddressMapper.reqAddressToAddress(addressRequest);
        address.setUser(findIdByUsername(principal.getName()));

        try {
            this.addressService.saveAddress(address);
            return response(address, HttpStatus.CREATED);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("edit")
    public ResponseEntity<?> editAddress(@RequestBody AddressUpdate addressUpdate, 
                                    Principal principal) {
        if (ObjectUtils.isEmpty(addressUpdate))
            return response("No param", HttpStatus.BAD_REQUEST);
        
        try {
            Address address = this.addressService.findAddress(addressUpdate.getId());

            if (!address.getUser().equals(findIdByUsername(principal.getName()))) {
                return response(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }

            if (addressUpdate.getProvince() != null)       address.setProvince(addressUpdate.getProvince());
            if (addressUpdate.getDistrict() != null)       address.setDistrict(addressUpdate.getDistrict());
            if (addressUpdate.getWard() != null)           address.setWard(addressUpdate.getWard());

            this.addressService.saveAddress(address);
            return response(address, HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable ObjectId id, Principal principal) {
        try {
            Address address = this.addressService.findAddress(id);

            if (!address.getUser().equals(findIdByUsername(principal.getName()))) {
                return response(null, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }

            this.addressService.deleteAddress(id);
            return response("Delete success", HttpStatus.OK);
        } catch (MongoException e) {
            return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
