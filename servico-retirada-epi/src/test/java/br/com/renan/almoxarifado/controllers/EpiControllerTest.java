package br.com.renan.almoxarifado.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.renan.almoxarifado.dtos.EpiResponse;
import br.com.renan.almoxarifado.services.EpiService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EpiController.class)
class EpiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EpiService service;

    @Test
    void givenValidPayload_whenPostEpi_thenReturns201() throws Exception {
        EpiResponse response = new EpiResponse(1L, "Luva", "desc", "url", 1L, 50, LocalDateTime.now());
        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/epis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Luva\",\"description\":\"desc\",\"documentUrl\":\"url\",\"epiCategoryId\":1,\"currentStock\":50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void givenNegativeStock_whenPostEpi_thenReturns400() throws Exception {
        mockMvc.perform(post("/epis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Luva\",\"description\":\"desc\",\"documentUrl\":\"url\",\"epiCategoryId\":1,\"currentStock\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetEpiDoDia_thenReturns200() throws Exception {
        EpiResponse response = new EpiResponse(1L, "Luva", "desc", "url", 1L, 50, LocalDateTime.now());
        when(service.randomOfTheDay()).thenReturn(response);

        mockMvc.perform(get("/epis/dia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luva"));
    }
}
