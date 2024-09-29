package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.entity.User;
import org.bson.types.ObjectId;

public interface UserService {
    User saveUser(User user);
    String checkUser(RegisterRequest register);
    User findUser(ObjectId userId);
}
