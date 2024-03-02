package com.spring_boot_testing.service;

import com.spring_boot_testing.entity.Employee;
import com.spring_boot_testing.repository.EmployeeRepository;
import com.spring_boot_testing.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class) // dodawać tylko jak korzystamy z adnotacji
class EmployeeServiceTest {
    //   test saveEmployee(Employee employee), który zwraca wyjątek
    //   test getAllEmployees() - pozytywny scenariusz
    //   test getAllEmployees() - negatywny scenariusz
    //   test getEmployeeById(long id);
    //   test updateEmployee(Employee updatedEmployee);
    //   test deleteEmployee(long id);
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    @BeforeEach
    void setUp(){
        employee = Employee.builder()
//                .id(1L)
                .firstName("Adam")
                .lastName("Małysz")
                .email("adam@gmail.com")
                .build();
    }

    @Test
    @DisplayName("Zapisywanie pracownika")
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        // given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then
        assertThat(savedEmployee).isNotNull();
//        assertThat(savedEmployee.getId()).isGreaterThan(0); // aby to odwzorować musimy mieć możliwość ustawiania ID
        assertThat(savedEmployee).isEqualTo(employee);
    }
}