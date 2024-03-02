package com.spring_boot_testing.controller;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
class EmployeeControllerTest {

    // testy tworzenia pracownika
    // pobierania pracowników
    // pobierania pracownika po id (pozytywny i negatywny scenariusz)
    // uaktualniane pracownika (pozytywny o negatywny scenariusz)
    // usuwania pracownika
    @Autowired
    private MockMvc mockMvc; // niezbędne do testów MVC - aby można było wywoływać endpointy

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper; // do deserializacji JSON na obiekt oraz serializacji

    @Test
    @DisplayName("testy tworzenia pracownika")
    void x() throws Exception {
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

}