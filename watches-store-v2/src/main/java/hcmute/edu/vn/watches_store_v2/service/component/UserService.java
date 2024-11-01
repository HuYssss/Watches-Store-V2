package hcmute.edu.vn.watches_store_v2.service.component;

import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileResponse;
import hcmute.edu.vn.watches_store_v2.entity.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    String checkUser(RegisterRequest register);
    User findUser(ObjectId userId);

    List<ProfileResponse> getAllUser();
    User blockUser(ObjectId userId, String message);
    User unblockUser(ObjectId userId);
    User deleteUser(ObjectId userId);
}
