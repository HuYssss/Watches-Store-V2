package hcmute.edu.vn.watches_store_v2.service.business;


import hcmute.edu.vn.watches_store_v2.dto.user.request.ProfileRequest;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileResponse;
import hcmute.edu.vn.watches_store_v2.entity.User;
import org.bson.types.ObjectId;

public interface ProfileService {
    ProfileResponse getProfileUser(ObjectId userId);
    User updateProfile(ProfileRequest profileRequest, ObjectId userId);
}
