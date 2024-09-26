package com.example.WatchesStoreV2.config.user;

import com.example.WatchesStoreV2.entity.User;
import com.example.WatchesStoreV2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class InitialUser implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
//        User manager = new User();
//        manager.setUserName("Manager");
//        manager.setPassword(passwordEncoder.encode("password"));
//        manager.setRoles("ROLE_MANAGER");
//        manager.setEmail("manager@manager.com");
//
//        User admin = new User();
//        admin.setUserName("Admin");
//        admin.setPassword(passwordEncoder.encode("password"));
//        admin.setRoles("ROLE_ADMIN");
//        admin.setEmail("admin@admin.com");
//
//        User user = new User();
//        user.setUserName("User");
//        user.setPassword(passwordEncoder.encode("password"));
//        user.setRoles("ROLE_USER");
//        user.setEmail("user@user.com");
//
//        userRepository.saveAll(List.of(manager,admin,user));
    }
}
