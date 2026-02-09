package com.siddardha.redisDemonstration.Exception;

public class EmployeeNotFoundException extends RuntimeException {


    public EmployeeNotFoundException(String message) {
        super(message);
    }

}
