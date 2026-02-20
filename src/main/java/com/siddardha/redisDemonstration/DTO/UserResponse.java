package com.siddardha.redisDemonstration.DTO;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String password;
}
