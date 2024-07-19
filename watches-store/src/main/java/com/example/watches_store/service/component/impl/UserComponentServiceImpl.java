package com.example.watches_store.service.component.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.watches_store.dto.UserDto.Request.UserRequest;
import com.example.watches_store.entity.User;
import com.example.watches_store.repository.UserRepository;
import com.example.watches_store.service.component.UserComponentService;
import com.mongodb.MongoException;

@Service
public class UserComponentServiceImpl implements UserComponentService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        try {
            this.userRepository.save(user);
            return user;
        } catch (MongoException e) {
            throw new MongoException("Can't save user");
        }
    }

    @Override
    public String checkUser(UserRequest userRequest) {
        if (this.userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            return "email";
        } 
        else if (this.userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            return "username";
        }

        return "ok";
    }
    
}
