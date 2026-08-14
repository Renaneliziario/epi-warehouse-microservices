package br.com.renan.almoxarifado.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import br.com.renan.almoxarifado.dtos.EmployeeRequest;
import br.com.renan.almoxarifado.dtos.EmployeeResponse;
import br.com.renan.almoxarifado.entities.Employee;
import br.com.renan.almoxarifado.exceptions.EmployeeNotFoundException;
import br.com.renan.almoxarifado.repositories.EmployeeRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    @Test
    void givenValidRequest_whenCreate_thenReturnsResponseWithGeneratedId() {
        EmployeeRequest request = new EmployeeRequest("Renan", "renan@teste.com");
        Employee saved = new Employee(1L, "Renan", "renan@teste.com", LocalDateTime.now());
        when(repository.save(org.mockito.ArgumentMatchers.any())).thenReturn(saved);

        EmployeeResponse response = service.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("renan@teste.com");
    }

    @Test
    void givenExistingId_whenFindById_thenReturnsResponse() {
        Employee employee = new Employee(1L, "Renan", "renan@teste.com", LocalDateTime.now());
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = service.findById(1L);

        assertThat(response.getName()).isEqualTo("Renan");
    }

    @Test
    void givenMissingId_whenFindById_thenThrowsNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("99");
    }
}
