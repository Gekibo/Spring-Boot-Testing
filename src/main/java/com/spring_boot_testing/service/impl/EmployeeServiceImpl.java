package com.spring_boot_testing.service.impl;

import com.spring_boot_testing.entity.Employee;
import com.spring_boot_testing.exceptions.EmployeeAlreadyExists;
import com.spring_boot_testing.repository.EmployeeRepository;
import com.spring_boot_testing.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent())
            throw new EmployeeAlreadyExists("Employee already exists with given email: " + employee.getEmail());
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
//        return updateEmployee(updatedEmployee.getId(), updatedEmployee).get()
        return employeeRepository.save(updatedEmployee);
    }
    @Override
    public Optional<Employee> updateEmployee(long id, Employee updatedEmployee) {
        return findById(id)
                .map(savedEmployee ->{
                    savedEmployee.setFirstName(updatedEmployee.getFirstName());
                    savedEmployee.setLastName(updatedEmployee.getLastName());
                    savedEmployee.setEmail(updatedEmployee.getEmail());
                    return employeeRepository.save(savedEmployee);
                });
    }



    @Override
    public void deleteById(long id) {
        employeeRepository.deleteById(id);
    }
}