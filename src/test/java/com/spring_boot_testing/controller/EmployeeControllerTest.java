package com.spring_boot_testing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_boot_testing.entity.Employee;
import com.spring_boot_testing.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
class EmployeeControllerTest {

    // testy tworzenia pracownika
    // pobierania pracowników
    // pobierania pracownika po id (pozytywny o negatywny scenariusz)
    // uaktualniane pracownika (pozytywny o negatywny scenariusz)
    // usuwania pracownika
    @Autowired
    private MockMvc mockMvc; // niezbędne do testów MVC - aby można było wywoływać endpointy

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper; // do deserializacji JSON na obiekt oraz serializacji

    @Test
    @DisplayName("test tworzenia pracownika")
    void givenEmployee_whenCreatedEmployee_thenReturnSavedEmployee() throws Exception {
        // given
        Employee employee = Employee.builder()
                .firstName("Adam")
                .lastName("Małysz")
                .email("adam@gmail.com")
                .build();

        given(employeeService.save(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0)); // ustawiamy '0' ponieważ 'save' ma tylko jeden parametr o indeksie '0'
        // when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @DisplayName("Test pobierania pracowników")
    @Test
    void givenListOfEmployees_whenFindAll_thenReturnEmployeeList() throws Exception {
        // given
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Adam").lastName("Małysz").email("adam@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Marek").lastName("Zegarek").email("jarek@gmail.com").build());
        given(employeeService.findAll()).willReturn(listOfEmployees);

        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @DisplayName("Pobierania pracownika po id (pozytywny scenariusz)")
    @Test
    void givenEmployeeId_whenFindById_thenReturnEmployee() throws Exception {
        // given
        Employee employee = Employee.builder().id(1L).firstName("Adam").lastName("Małysz").email("adam@gmail.com").build();
        long employeeId = employee.getId();
        given(employeeService.findById(employeeId)).willReturn(Optional.of(employee));

        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andDo(print())
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @DisplayName("Pobierania pracownika po id (negatywny scenariusz)")
    @Test
    void givenInvalidEmployeeId_whenFindById_thenReturnNotFound() throws Exception {
        // given
        given(employeeService.findById(anyLong())).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1L));

        // then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @DisplayName("Uaktualniane pracownika (pozytywny scenariusz)")
    @Test
    void givenEmployeeId_whenUpdated_thenReturnEmployee() throws Exception {
        // given
        Employee employee = Employee.builder().id(1L).firstName("Adam").lastName("Małysz").email("adam@gmail.com").build();
        long employeeId = employee.getId();
        given(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).willReturn(Optional.of(employee));

        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @DisplayName("Uaktualniane pracownika (pozytywny negatywny)")
    @Test
    void givenInvalidEmployeeId_whenUpdated_thenReturnEmployee() throws Exception {
        // given
        Employee employee = Employee.builder().id(1L).firstName("Adam").lastName("Małysz").email("adam@gmail.com").build();
        long employeeId = 999L;
        given(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());

    }

    @DisplayName("Usuwania pracownika")
    @Test
    void givenEmployeeId_whenDeleted_thenReturn200() throws Exception {
        // given
        long employeeId = 1l;
        willDoNothing().given(employeeService).deleteById(employeeId);

        // when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }

}