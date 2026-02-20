package com.siddardha.redisDemonstration.Mapper;

import com.siddardha.redisDemonstration.DTO.UserResponse;
import com.siddardha.redisDemonstration.Model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse mapToResponse(User user) {

        UserResponse response  = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        return response;
    }
}
