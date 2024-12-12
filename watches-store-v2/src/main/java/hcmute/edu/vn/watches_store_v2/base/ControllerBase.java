package hcmute.edu.vn.watches_store_v2.base;

import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ControllerBase {
    @Autowired
    private UserRepository userRepository;

    public ObjectId findIdByUsername(String username) {
        Optional<User> currentUser = this.userRepository.findByUsername(username);

        if (currentUser.isPresent())
            return currentUser.get().getId();
        else
            return null;
    }

    public ResponseEntity<?> response(Object object, HttpStatus httpStatus) {
        return new ResponseEntity<Object>(object, httpStatus);
    }
}
