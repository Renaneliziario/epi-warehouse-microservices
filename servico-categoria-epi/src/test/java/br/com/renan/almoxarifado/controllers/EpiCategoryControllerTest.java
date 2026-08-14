package br.com.renan.almoxarifado.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.renan.almoxarifado.dtos.EpiCategoryResponse;
import br.com.renan.almoxarifado.services.EpiCategoryService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EpiCategoryController.class)
class EpiCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EpiCategoryService service;

    @Test
    void givenValidPayload_whenPostEpiCategory_thenReturns201() throws Exception {
        EpiCategoryResponse response = new EpiCategoryResponse(1L, "Protecao das maos", "luvas", LocalDateTime.now());
        when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/epi-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Protecao das maos\",\"description\":\"luvas\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void givenBlankName_whenPostEpiCategory_thenReturns400() throws Exception {
        mockMvc.perform(post("/epi-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":\"luvas\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenExistingId_whenGetExists_thenReturns204() throws Exception {
        when(service.exists(1L)).thenReturn(true);

        mockMvc.perform(get("/epi-categories/1/exists"))
                .andExpect(status().isNoContent());
    }
}
