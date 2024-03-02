package com.spring_boot_testing.repository;

import com.spring_boot_testing.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // używa domyślnie in-memory database, testy są wykonywane w ramach transakcji, jest robiony Roll-Back na koniec testu
//@SpringBootTest
class EmployeeRepositoryTest {
    // TDD - test driven development - najpierw piszemy test a potem metody
    // BDD - behavior driven development- dzielenie na sekcje:
    // given - aktualne wartości, obiekty
    // when - akcja lub testowane zachowanie
    // then - weryfikacja wyniku
    @Autowired
    private EmployeeRepository employeeRepository; // w testach wstrzykujemy przez pole

    private Employee employee;
    @BeforeEach
    void setUp(){
        employee = Employee.builder()
                .firstName("Adam")
                .lastName("Małysz")
                .email("adam@gmail.com")
                .build();
    }

    @Test
    @DisplayName("Zapisywanie pracownika")
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        // given
        // when
        Employee savedEmployee = employeeRepository.save(employee);

        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
        assertThat(savedEmployee).isEqualTo(employee);
    }

    @DisplayName("pobieranie wszystkich pracowników")
    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmployeeList(){
        // given
        Employee employee2 = Employee.builder()
                .firstName("Adam")
                .lastName("Małysz")
                .email("adam@gmail.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when
        List<Employee> employeeList = employeeRepository.findAll();

        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }
    @DisplayName("Pobieranie pracowników po id")
    @Test
    void givenEmployeeObject_whenFindById_thenReturnEmployee(){
        // given
        employeeRepository.save(employee);

        // when
        Employee employeeById = employeeRepository.findById(employee.getId()).get();

        // then
        assertThat(employeeById).isNotNull();
        assertThat(employeeById).isEqualTo(employee);
    }
    @DisplayName("Pobieranie pracowników po email")
    @Test
    void givenEmployee_whenFindByEmail_thenReturnEmployee(){
        // given
        employeeRepository.save(employee);

        //when
        Employee employeeByEmail = employeeRepository.findByEmail(employee.getEmail()).get();

        //then
        assertThat(employeeByEmail).isNotNull();
        assertThat(employeeByEmail).isEqualTo(employee);
    }

    @DisplayName("Uaktualnianie danych pracowników")
    @Test
    void givenEmployeeWithChangedFieldsWhenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given
        employeeRepository.save(employee);
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("jarek@gimail.com");
        savedEmployee.setFirstName("Jarek");

        // when
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then
        assertThat(updatedEmployee.getEmail()).isEqualTo("jarek@gimail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jarek");
    }

    @DisplayName("Usuwanie pracownika")
    @Test
    void givenEmployee_whenDelete_thenEmployeeNotPresent() {
        // given
        employeeRepository.save(employee);

        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then
        assertThat(employeeOptional).isEmpty();

    }

    @DisplayName("Testy dla zapytań z indeksowanymi parametrami")
    @Test
    void givenEmployee_whenFindByFirstNameAndLastName_thenReturnEmployee(){
        // given
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        Employee foundEmployee = employeeRepository.findByFirstNameAndLastName(savedEmployee.getFirstName(), savedEmployee.getLastName()).get();

        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(savedEmployee);

    }

    @Test
    void givenEmployee_whenFindByJPQL_thenReturnEmployee(){
        // given
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        Employee foundEmployee = employeeRepository.findByJPQL(savedEmployee.getFirstName(), savedEmployee.getLastName()).get();

        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(savedEmployee);

    }

    @Test
    void givenEmployee_whenFindByJPQLNative_thenReturnEmployee(){
        // given
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        Employee foundEmployee = employeeRepository.findByNativeSQL(savedEmployee.getFirstName(), savedEmployee.getLastName()).get();

        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(savedEmployee);

    }
    // testy dla zapytań z nazwami parametrów

    @Test
    void givenEmployee_whenFindByJPQLNamedParams_thenReturnEmployee(){
        // given
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        Employee foundEmployee = employeeRepository.findByJPQLNamedParams(savedEmployee.getFirstName(), savedEmployee.getLastName()).get();

        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(savedEmployee);

    }
    @Test
    void givenEmployee_whenFindByNativeSqlNamedParams_thenReturnEmployee(){
        // given
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        Employee foundEmployee = employeeRepository.findByNativeSqlNamedParams(savedEmployee.getFirstName(), savedEmployee.getLastName()).get();

        // then
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(savedEmployee);

    }
}