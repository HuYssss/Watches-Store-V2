package com.example.watches_store.dto.UserDto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String email;
	private String phone;
	private String username;
	private String password;
}
