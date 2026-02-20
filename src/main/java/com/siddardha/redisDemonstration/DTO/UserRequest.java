package com.siddardha.redisDemonstration.DTO;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
}
