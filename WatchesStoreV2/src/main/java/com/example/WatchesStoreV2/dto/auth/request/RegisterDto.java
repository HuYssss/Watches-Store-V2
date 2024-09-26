package com.example.WatchesStoreV2.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String email;
    private String phone;
    private String username;
    private String password;
}
