package br.com.renan.almoxarifado.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.renan.almoxarifado.dtos.EpiWithdrawalResponse;
import br.com.renan.almoxarifado.exceptions.EmployeeNotFoundException;
import br.com.renan.almoxarifado.services.EpiWithdrawalService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EpiWithdrawalController.class)
class EpiWithdrawalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EpiWithdrawalService service;

    @Test
    void givenValidPayload_whenPostRetirada_thenReturns201() throws Exception {
        EpiWithdrawalResponse response = new EpiWithdrawalResponse(1L, 1L, 1L, 5, LocalDateTime.now());
        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/retiradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"epiId\":1,\"employeeId\":1,\"quantity\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void givenMissingEmployee_whenPostRetirada_thenReturns422() throws Exception {
        when(service.create(any())).thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(post("/retiradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"epiId\":1,\"employeeId\":999,\"quantity\":1}"))
                .andExpect(status().isUnprocessableEntity());
    }
}
