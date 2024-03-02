package com.spring_boot_testing.repository;

import com.spring_boot_testing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // JPQL - Java Persistence Query Language
    Optional<Employee> findByEmail(String email); // JPQL -  na podstawie słów kluczowych tworzone jest zapytanie, wynika to z 'Spring Data JPA'

    Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);
    // To samo co powyżej za pomocą JPQL z użyciem własnego zapytania
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Optional<Employee> findByJPQL(String firstName, String lastName);

    @Query("select e from Employee e where e.firstName = :xyz and e.lastName = :lastName")
    Optional<Employee> findByJPQLNamedParams(@Param("xyz")String firstName, String lastName); // @Param() dodajemy tylko gdy nazwa parametru jest inna niż nazwa pola


    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2",
            nativeQuery = true)
    Optional<Employee> findByNativeSQL(String firstName, String lastName);

    @Query(value = "select * from employees e where e.first_name = :xyz and e.last_name = :lastName"
            , nativeQuery = true)
    Optional<Employee> findByNativeSqlNamedParams(@Param("xyz")String firstName, String lastName);
}