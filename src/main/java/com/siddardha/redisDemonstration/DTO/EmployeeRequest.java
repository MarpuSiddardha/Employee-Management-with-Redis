package com.siddardha.redisDemonstration.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequest {

    @NotNull(message = "name should not be blank or null")
    private String name;

    @NotNull(message = "Role must be present. ")
    private String role;

    @NotNull(message = "salary should not be null or empty")
    private Double salary;

}
