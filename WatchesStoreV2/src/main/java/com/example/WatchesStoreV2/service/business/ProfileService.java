package com.example.WatchesStoreV2.service.business;

import com.example.WatchesStoreV2.dto.user.request.ProfileRequest;
import com.example.WatchesStoreV2.dto.user.response.ProfileResponse;
import com.example.WatchesStoreV2.entity.User;
import org.bson.types.ObjectId;

public interface ProfileService {
    ProfileResponse getProfileUser(ObjectId userId);
    User updateProfile(ProfileRequest profileRequest, ObjectId userId);
}
