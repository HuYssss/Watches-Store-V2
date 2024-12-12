package hcmute.edu.vn.watches_store_v2.mapper;

import hcmute.edu.vn.watches_store_v2.dto.auth.request.RegisterRequest;
import hcmute.edu.vn.watches_store_v2.dto.auth.response.AuthResponseDto;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileOrder;
import hcmute.edu.vn.watches_store_v2.dto.user.response.ProfileResponse;
import hcmute.edu.vn.watches_store_v2.entity.Order;
import hcmute.edu.vn.watches_store_v2.entity.User;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserMapper {
    public static User mapUser(RegisterRequest register) {
        return new User(
                new ObjectId(),
                register.getEmail(),
                null,
                register.getUsername(),
                register.getPassword(),
                null,
                null,
                null,
                "ROLE_USER",
                "active",
                "Local",
                null,
                new Date(),
                null,
                false,
                null,
                null
        );
    }

    public static ProfileResponse convertProfileUser(User user) {
        return new ProfileResponse(
                user.getId().toHexString(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                user.getFullName(),
                user.getAvatarImg(),
                user.getAddress(),
                user.getReasonBlock(),
                user.getCreatedAt(),
                user.getBlockAt(),
                user.getState(),
                user.isVerified(),
                user.getRoles().equals("ROLE_ADMIN"),
                new ArrayList<>()
        );
    }

    public static ProfileOrder mapProfileOrder (User user) {
        return new ProfileOrder(
                user.getFullName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress()
        );
    }

    public static AuthResponseDto mapAuthResponse(User user, String accessToken, String refreshToken) {
        return new AuthResponseDto(
                accessToken,
                refreshToken,
                user.getId().toHexString(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                user.getFullName(),
                user.getAvatarImg(),
                user.getAddress(),
                user.getState(),
                user.isVerified(),
                user.getRoles().equals("ROLE_ADMIN")
        );
    }
}
