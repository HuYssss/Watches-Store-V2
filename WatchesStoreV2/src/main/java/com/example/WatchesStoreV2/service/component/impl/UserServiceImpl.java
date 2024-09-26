package com.example.WatchesStoreV2.service.component.impl;

import com.example.WatchesStoreV2.dto.auth.request.RegisterDto;
import com.example.WatchesStoreV2.entity.User;
import com.example.WatchesStoreV2.repository.UserRepository;
import com.example.WatchesStoreV2.service.component.UserService;
import com.mongodb.MongoException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        try {
            this.userRepository.save(user);
            return user;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't save user");
        }
    }

    @Override
    public String checkUser(RegisterDto userRequest) {
        if (this.userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            return "email";
        }
        else if (this.userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            return "username";
        }

        return "ok";
    }

    @Override
    public User findUser(ObjectId userId) {
        try {
            return this.userRepository.findById(userId).orElse(null);
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't find user");
        }
    }
}
