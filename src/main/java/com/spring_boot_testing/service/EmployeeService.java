package com.spring_boot_testing.service;

import com.spring_boot_testing.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee save(Employee employee);
    List<Employee> findAll();
    Optional<Employee> FindById(long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteById(long id);
}
