package com.example.WatchesStoreV2.service.component;

import com.example.WatchesStoreV2.dto.auth.request.RegisterDto;
import com.example.WatchesStoreV2.entity.User;
import org.bson.types.ObjectId;

public interface UserService {
    User saveUser(User user);
    String checkUser(RegisterDto registerDto);
    User findUser(ObjectId userId);
}
