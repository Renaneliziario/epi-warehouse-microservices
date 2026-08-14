package br.com.renan.almoxarifado.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.renan.almoxarifado.dtos.EmployeeResponse;
import br.com.renan.almoxarifado.services.EmployeeService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @Test
    void givenValidPayload_whenPostEmployee_thenReturns201() throws Exception {
        EmployeeResponse response = new EmployeeResponse(1L, "Renan", "renan@teste.com", LocalDateTime.now());
        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Renan\",\"email\":\"renan@teste.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void givenInvalidEmail_whenPostEmployee_thenReturns400() throws Exception {
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Renan\",\"email\":\"nao-e-email\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenExistingId_whenGetExists_thenReturns204() throws Exception {
        when(service.exists(1L)).thenReturn(true);

        mockMvc.perform(get("/employees/1/exists"))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenMissingId_whenGetExists_thenReturns404() throws Exception {
        when(service.exists(99L)).thenReturn(false);

        mockMvc.perform(get("/employees/99/exists"))
                .andExpect(status().isNotFound());
    }
}
