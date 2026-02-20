package com.siddardha.redisDemonstration.DTO;

import lombok.Data;

@Data
public class EmployeeResponse {
    private Long id;
    private String employeeId;
    private String name;
    private String role;
    private double salary;
}
