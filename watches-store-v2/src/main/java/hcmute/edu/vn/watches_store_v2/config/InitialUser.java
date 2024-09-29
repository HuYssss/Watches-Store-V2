package hcmute.edu.vn.watches_store_v2.config;

import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
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
//        manager.setUsername("Manager");
//        manager.setPassword(passwordEncoder.encode("password"));
//        manager.setRoles("ROLE_MANAGER");
//        manager.setEmail("manager@manager.com");
//
//        User admin = new User();
//        admin.setUsername("Admin");
//        admin.setPassword(passwordEncoder.encode("password"));
//        admin.setRoles("ROLE_ADMIN");
//        admin.setEmail("admin@admin.com");
//
//        User user = new User();
//        user.setUsername("User");
//        user.setPassword(passwordEncoder.encode("password"));
//        user.setRoles("ROLE_USER");
//        user.setEmail("user@user.com");
//
//        userRepository.saveAll(List.of(manager,admin,user));
    }
}
