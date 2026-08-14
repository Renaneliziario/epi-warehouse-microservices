package br.com.renan.almoxarifado.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.renan.almoxarifado.dtos.EpiCategoryRequest;
import br.com.renan.almoxarifado.dtos.EpiCategoryResponse;
import br.com.renan.almoxarifado.entities.EpiCategory;
import br.com.renan.almoxarifado.exceptions.EpiCategoryNotFoundException;
import br.com.renan.almoxarifado.repositories.EpiCategoryRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EpiCategoryServiceTest {

    @Mock
    private EpiCategoryRepository repository;

    @InjectMocks
    private EpiCategoryService service;

    @Test
    void givenValidRequest_whenCreate_thenReturnsResponseWithGeneratedId() {
        EpiCategoryRequest request = new EpiCategoryRequest("Protecao das maos", "luvas de seguranca");
        EpiCategory saved = new EpiCategory(1L, "Protecao das maos", "luvas de seguranca", LocalDateTime.now());
        when(repository.save(any())).thenReturn(saved);

        EpiCategoryResponse response = service.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Protecao das maos");
    }

    @Test
    void givenExistingId_whenFindById_thenReturnsResponse() {
        EpiCategory category = new EpiCategory(1L, "Protecao das maos", "luvas de seguranca", LocalDateTime.now());
        when(repository.findById(1L)).thenReturn(Optional.of(category));

        EpiCategoryResponse response = service.findById(1L);

        assertThat(response.getDescription()).isEqualTo("luvas de seguranca");
    }

    @Test
    void givenMissingId_whenFindById_thenThrowsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(EpiCategoryNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void givenExistingId_whenExists_thenReturnsTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        assertThat(service.exists(1L)).isTrue();
    }
}
