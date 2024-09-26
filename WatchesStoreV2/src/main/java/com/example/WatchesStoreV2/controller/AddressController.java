package com.example.WatchesStoreV2.controller;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.dto.address.request.AddressRequest;
import com.example.WatchesStoreV2.dto.address.request.AddressUpdate;
import com.example.WatchesStoreV2.entity.Address;
import com.example.WatchesStoreV2.mapper.AddressMapper;
import com.example.WatchesStoreV2.service.component.AddressService;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.ObjectUtils;

import java.security.Principal;

@RestController
@RequestMapping("api/address")
public class AddressController extends ControllerBase {

    @Autowired
    private AddressService addressService;

    @GetMapping("")
    public ResponseEntity<?> getUserAddresses(Principal principal) {
        try {
            return response(this.addressService.getUserAddresses(findIdByUsername(principal.getName()))
                    , HttpStatus.OK);
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
            return response(this.addressService.saveAddress(address), HttpStatus.CREATED);
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

            return response(this.addressService.saveAddress(address), HttpStatus.OK);
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
