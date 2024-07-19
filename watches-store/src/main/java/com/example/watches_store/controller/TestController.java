package com.example.watches_store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/testLogin")
    public String getMethodName(Principal principal) {
        return principal.getName();
    }
    
}
