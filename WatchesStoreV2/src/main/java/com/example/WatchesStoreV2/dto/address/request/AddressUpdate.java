package com.example.WatchesStoreV2.dto.address.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdate {
    private ObjectId id;

    private String province;

    private String district;

    private String ward;
}
