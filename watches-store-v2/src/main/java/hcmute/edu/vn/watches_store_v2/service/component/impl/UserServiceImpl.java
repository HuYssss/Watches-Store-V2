package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.mapper.UserMapper;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
import hcmute.edu.vn.watches_store_v2.service.component.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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
    public String checkUser(RegisterRequest request) {
        if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "email";
        }
        else if (this.userRepository.findByUsername(request.getUsername()).isPresent()) {
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
