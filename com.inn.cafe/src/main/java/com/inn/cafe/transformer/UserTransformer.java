package com.inn.cafe.transformer;

import com.inn.cafe.dto.UserResponseDto;
import com.inn.cafe.pojo.User;

import java.util.Map;

public class UserTransformer {

    public static User userResponseDtoToUser(UserResponseDto userResponseDto){
        return User.builder()
                .name(userResponseDto.getName())
                .contactNumber(userResponseDto.getContactNumber())
                .email(userResponseDto.getEmail())
                .password(userResponseDto.getPassword())
                .build();
    }

    public static UserResponseDto userRequestMapToUserResponseDto(Map<String,String> userRequestMap){
        return UserResponseDto.builder()
                .name(userRequestMap.get("name"))
                .email(userRequestMap.get("email"))
                .contactNumber(userRequestMap.get("contactNumber"))
                .password(userRequestMap.get("password"))
                .build();
    }
}
