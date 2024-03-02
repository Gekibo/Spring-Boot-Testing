package com.spring_boot_testing.exceptions;

public class EmployeeAlreadyExists extends RuntimeException{
    public EmployeeAlreadyExists(String message) {
        super(message);
    }
}
