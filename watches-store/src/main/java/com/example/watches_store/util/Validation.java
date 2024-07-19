package com.example.watches_store.util;

import java.text.ParseException;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

import com.example.watches_store.dto.UserDto.Request.UserRequest;
import com.example.watches_store.exception.InvalidValueException;
import com.example.watches_store.exception.NoParamException;


public class Validation {
    
    public static void validate(UserRequest userRequest)
            throws InvalidValueException, NoParamException, ParseException{
        if (ObjectUtils.isEmpty(userRequest))
            throw new NoParamException();
        if (ObjectUtils.isEmpty(userRequest.getEmail()) || ObjectUtils.isEmpty(userRequest.getPhone()) ||
                ObjectUtils.isEmpty(userRequest.getUsername()) || ObjectUtils.isEmpty(userRequest.getPassword()))
            throw new NoParamException();
        if (!isValidEmail(userRequest.getEmail()) || userRequest.getPhone().length() != 10)
            throw new InvalidValueException();
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
