package com.example.watches_store.service.business;

import com.example.watches_store.dto.UserDto.Request.AuthRequest;
import com.example.watches_store.dto.UserDto.Request.UserRequest;
import com.example.watches_store.entity.User;

public interface UserService {
    User register(UserRequest userRequest);
    String login(AuthRequest authRequest);
}
