package com.siddardha.redisDemonstration.Mapper;

import com.siddardha.redisDemonstration.DTO.EmployeeResponse;
import com.siddardha.redisDemonstration.Model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public static EmployeeResponse mapToResponse(Employee emp) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(emp.getId());
        response.setEmployeeId(emp.getEmployeeId());
        response.setName(emp.getName());
        response.setRole(emp.getRole());
        response.setSalary(emp.getSalary());
        return response;
    }

}
