package com.example.WatchesStoreV2.controller;

import com.example.WatchesStoreV2.base.ControllerBase;
import com.example.WatchesStoreV2.service.component.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController extends ControllerBase {

    private final ProductService productService;

    @GetMapping("/hello")
    public String testHello() {
        return "Hello World";
    }

    @GetMapping("/testMail")
    public String testMail() {

        return "Mail sent !!!";
    }

    @PreAuthorize("hasAuthority('SCOPE_ACCESS_FULL_SYSTEM')")
    @GetMapping("/test")
    public String testScopeUser() {
        return "Hello Admin";
    }
}
