package com.example.watches_store.service.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.watches_store.dto.UserDto.Request.AuthRequest;
import com.example.watches_store.dto.UserDto.Request.UserRequest;
import com.example.watches_store.entity.User;
import com.example.watches_store.helper.MailService;
import com.example.watches_store.mapper.UserMapper;
import com.example.watches_store.service.business.UserService;
import com.example.watches_store.service.component.UserComponentService;
import com.example.watches_store.util.JwtUtils;
import com.mongodb.MongoException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserComponentService userComponentService;

    @Autowired
    private MailService mailService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    public User register(UserRequest userRequest) {

        try {
            User user = UserMapper.resUserToUser(userRequest);
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            
            this.userComponentService.saveUser(user);
            this.mailService.welcome(userRequest.getEmail(), user.getUsername());

            return user;
        } catch (MongoException e) {
            return null;
        }

    }

    @Override
    public String login(AuthRequest authRequest) {
        
        try {
            this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), 
                    authRequest.getPassword()
                )
            );

            return JwtUtils.generateToken(authRequest.getUsername());
        } catch (Exception e) {
            return "error";
        }
    }
    
}
