package com.example.WatchesStoreV2.mapper;

import com.example.WatchesStoreV2.dto.address.response.AddressResponse;
import com.example.WatchesStoreV2.dto.auth.request.RegisterDto;
import com.example.WatchesStoreV2.dto.auth.response.AuthResponseDto;
import com.example.WatchesStoreV2.dto.user.response.ProfileOrder;
import com.example.WatchesStoreV2.dto.user.response.ProfileResponse;
import com.example.WatchesStoreV2.entity.Address;
import com.example.WatchesStoreV2.entity.User;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class UserMapper {
    public static User registerUser(RegisterDto registerDto) {
        return new User(
            new ObjectId(),
            registerDto.getEmail(),
            registerDto.getPhone(),
            registerDto.getUsername(),
            registerDto.getPassword(),
            "unknow",
            "unknow",
            "unknow",
            "ROLE_USER",
            "active",
            "Local",
            false,
            "",
            null
        );
    }

    public static ProfileResponse convertProfileUser(User user, List<AddressResponse> addresses) {
        return new ProfileResponse(
                user.getId().toHexString(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                user.getFullName(),
                user.getAvatarImg(),
                user.getAddress(),
                user.getState(),
                user.isVerified(),
                false
        );
    }

    public static User mapNewEmptyUser() {
        return new User(
                new ObjectId(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "ROLE_USER",
                "active",
                "",
                false,
                "",
                null
        );
    }

    public static ProfileOrder mapProfileOrder (User user) {
        return new ProfileOrder(
                user.getFullName(),
                user.getPhone(),
                user.getAddress()
        );
    }

    public static AuthResponseDto mapAuthResponse(User user, String accessToken) {
        return new AuthResponseDto(
                accessToken,
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
