package com.example.watches_store.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.watches_store.base.ControllerBase;
import com.example.watches_store.dto.UserDto.Request.AuthRequest;
import com.example.watches_store.dto.UserDto.Request.UserRequest;
import com.example.watches_store.entity.User;
import com.example.watches_store.service.business.UserService;
import com.example.watches_store.service.component.UserComponentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/account")
public class AccountController extends ControllerBase {
    
    @Autowired
    private UserComponentService userComponentService;

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {

        String checkUser = this.userComponentService.checkUser(userRequest);

        if (checkUser.equals("email"))              
            return response("Email already registerd", HttpStatus.BAD_REQUEST);
        else if (checkUser.equals("username"))      
            return response("Duplicated username", HttpStatus.BAD_REQUEST);

        User user = this.userService.register(userRequest);

        if (user != null)           
            return response(user, HttpStatus.OK);
        else                        
            return response("Error in processing", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        if (ObjectUtils.isEmpty(authRequest) 
                || ObjectUtils.isEmpty(authRequest.getPassword())
                || ObjectUtils.isEmpty(authRequest.getUsername()))
            return response("No param", HttpStatus.BAD_REQUEST);
        
        String token = this.userService.login(authRequest);

        if (!token.equals("error")) 
            return response(token, HttpStatus.OK);
        
        return response("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
    
    
}
