package com.siddardha.redisDemonstration.DTO;

import lombok.Data;

@Data
public class EmployeePartialUpdateRequest {

    private String name;
    private String role;
    private Double salary;
}
