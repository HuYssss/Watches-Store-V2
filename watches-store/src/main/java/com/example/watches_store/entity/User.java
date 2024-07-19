package com.example.watches_store.entity;

import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "User")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    private String email;

    private String phone;

    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private String avatarImg;

    private String backgroundImg;
    
    private Set<Role> role;

    private String state;
    
    private String token;
}