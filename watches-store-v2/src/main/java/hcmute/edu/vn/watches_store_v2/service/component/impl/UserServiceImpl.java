package hcmute.edu.vn.watches_store_v2.service.component.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileResponse;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.mapper.UserMapper;
import hcmute.edu.vn.watches_store_v2.repository.OrderRepository;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
import hcmute.edu.vn.watches_store_v2.service.component.MailService;
import hcmute.edu.vn.watches_store_v2.service.component.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final OrderRepository orderRepository;

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

    @Override
    public List<ProfileResponse> getAllUser() {
        try {
            List<ProfileResponse> resp =  this.userRepository.findAll()
                    .stream()
                    .filter(user -> !user.getRoles().equals("ROLE_ADMIN"))
                    .map(UserMapper::convertProfileUser)
                    .collect(Collectors.toList());

            List<Order> orders = this.orderRepository.findAll();

            resp.forEach(r -> {
                orders.forEach(o -> {
                    if (o.getUserId().equals(new ObjectId(r.getId()))) {
                        r.setOrder(o);
                    }
                });
            });

            return resp;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't get all user");
        }
    }

    @Override
    public User blockUser(ObjectId userId, String message) {
        User user = findUser(userId);

        if (user != null && !user.getRoles().equals("ROLE_ADMIN")) {

            user.setState("blocked");
            user.setReasonBlock(message);
            user.setBlockAt(new Date());

            this.userRepository.save(user);

            this.mailService.blockUser(user.getEmail(), user.getUsername(), message);

            return user;
        }

        return null;
    }

    @Override
    public User unblockUser(ObjectId userId) {
        User user = findUser(userId);
        if (user != null && user.getState().equals("blocked")) {

            user.setState("active");
            user.setReasonBlock(null);
            user.setBlockAt(null);

            this.userRepository.save(user);

            this.mailService.unBlockUser(user.getEmail(), user.getUsername());
            return user;
        }

        return null;
    }

    @Override
    public User deleteUser(ObjectId userId) {
        return null;
    }
}
