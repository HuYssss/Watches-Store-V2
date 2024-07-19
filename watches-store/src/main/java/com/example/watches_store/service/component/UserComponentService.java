package com.example.watches_store.service.component;

import com.example.watches_store.dto.UserDto.Request.UserRequest;
import com.example.watches_store.entity.User;

public interface UserComponentService {
    User saveUser(User user);
    String checkUser(UserRequest userRequest);
}
