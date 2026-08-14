package br.com.renan.almoxarifado.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.renan.almoxarifado.clients.EpiCategoryClient;
import br.com.renan.almoxarifado.dtos.EpiRequest;
import br.com.renan.almoxarifado.dtos.EpiResponse;
import br.com.renan.almoxarifado.entities.Epi;
import br.com.renan.almoxarifado.exceptions.EpiCategoryNotFoundException;
import br.com.renan.almoxarifado.repositories.EpiRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EpiServiceTest {

    @Mock
    private EpiRepository repository;

    @Mock
    private EpiCategoryClient epiCategoryClient;

    @InjectMocks
    private EpiService service;

    @Test
    void givenExistingCategory_whenCreate_thenSavesEpi() {
        when(epiCategoryClient.exists(1L)).thenReturn(true);
        Epi saved = new Epi(1L, "Luva nitrilica", "desc", "url", 1L, 50, LocalDateTime.now());
        when(repository.save(any())).thenReturn(saved);

        EpiResponse response = service.create(new EpiRequest("Luva nitrilica", "desc", "url", 1L, 50));

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCurrentStock()).isEqualTo(50);
    }

    @Test
    void givenMissingCategory_whenCreate_thenThrowsEpiCategoryNotFound() {
        when(epiCategoryClient.exists(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.create(new EpiRequest("Luva", "desc", "url", 999L, 10)))
                .isInstanceOf(EpiCategoryNotFoundException.class);
    }

    @Test
    void givenItemsInCatalog_whenRandomOfTheDay_thenReturnsOne() {
        Epi epi = new Epi(1L, "Luva nitrilica", "desc", "url", 1L, 50, LocalDateTime.now());
        when(repository.findRandom()).thenReturn(Optional.of(epi));

        EpiResponse response = service.randomOfTheDay();

        assertThat(response.getName()).isEqualTo("Luva nitrilica");
    }
}
