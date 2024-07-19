package com.example.watches_store.mapper;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;

import com.example.watches_store.dto.UserDto.Request.UserRequest;
import com.example.watches_store.entity.Role;
import com.example.watches_store.entity.User;

public class UserMapper {
    public static User resUserToUser(UserRequest userRequest) {
        return new User(
            new ObjectId(),
            userRequest.getEmail(),
            userRequest.getPhone(),
            userRequest.getUsername(),
            userRequest.getPassword(),
            "unknow",
            "unknow",
            "unknow",
            "unknow",
            userRole(),
            "active",
            ""
        );
    }

    public static Set<Role> userRole() {
        Set<Role> roles = new HashSet<>();
        Role role = new Role();
        role.setId(new ObjectId("65bb1b854c79c0063ff039e2"));
        roles.add(role);

        return roles;
    }
}
