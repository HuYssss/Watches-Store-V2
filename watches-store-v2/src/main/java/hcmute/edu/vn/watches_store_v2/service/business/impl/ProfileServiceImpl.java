package hcmute.edu.vn.watches_store_v2.service.business.impl;

import com.mongodb.MongoException;
import hcmute.edu.vn.watches_store_v2.dto.user.request.ProfileRequest;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileResponse;
import hcmute.edu.vn.watches_store_v2.entity.User;
import hcmute.edu.vn.watches_store_v2.mapper.UserMapper;
import hcmute.edu.vn.watches_store_v2.service.business.ProfileService;
import hcmute.edu.vn.watches_store_v2.service.component.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserService userService;

    @Override
    public ProfileResponse getProfileUser(ObjectId userId) {
        try {
            User user = this.userService.findUser(userId);

            ProfileResponse profileResponse = UserMapper.convertProfileUser(user);

            if ((user.getRoles().contains("ROLE_ADMIN")))
                profileResponse.setAdmin(true);

            return profileResponse;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't convert profile user");
        }
    }

    @Override
    public User updateProfile(ProfileRequest profileRequest, ObjectId userId) {
        try {
            User user = this.userService.findUser(userId);

            if (profileRequest.getPhone() != null)              user.setPhone(profileRequest.getPhone());
            if (profileRequest.getFullName() != null)          user.setFullName(profileRequest.getFullName());
            if (profileRequest.getAvatarImg() != null)          user.setAvatarImg(profileRequest.getAvatarImg());
            if (profileRequest.getAddress() != null)            user.setAddress(profileRequest.getAddress());

            if (profileRequest.getEmail() != null) {
                if (!user.getEmail().equals(profileRequest.getEmail())) {
                    user.setVerified(false);
                    user.setTokenExpiryDate(null);
                    user.setToken(null);
                }
                user.setEmail(profileRequest.getEmail());
            }

            this.userService.saveUser(user);
            return user;
        } catch (MongoException e) {
            e.printStackTrace();
            throw new MongoException("Can't update profile user");
        }
    }
}
