package br.com.renan.almoxarifado.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.renan.almoxarifado.clients.EmployeeClient;
import br.com.renan.almoxarifado.dtos.EpiWithdrawalRequest;
import br.com.renan.almoxarifado.dtos.EpiWithdrawalResponse;
import br.com.renan.almoxarifado.entities.Epi;
import br.com.renan.almoxarifado.entities.EpiWithdrawal;
import br.com.renan.almoxarifado.exceptions.EmployeeNotFoundException;
import br.com.renan.almoxarifado.exceptions.EpiNotFoundException;
import br.com.renan.almoxarifado.exceptions.InsufficientStockException;
import br.com.renan.almoxarifado.repositories.EpiRepository;
import br.com.renan.almoxarifado.repositories.EpiWithdrawalRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EpiWithdrawalServiceTest {

    @Mock
    private EpiWithdrawalRepository withdrawalRepository;

    @Mock
    private EpiRepository epiRepository;

    @Mock
    private EmployeeClient employeeClient;

    @InjectMocks
    private EpiWithdrawalService service;

    @Test
    void givenValidRequest_whenCreate_thenDecrementsStockAndSaves() {
        Epi epi = new Epi(1L, "Luva", "desc", "url", 1L, 50, LocalDateTime.now());
        when(epiRepository.findById(1L)).thenReturn(Optional.of(epi));
        when(employeeClient.exists(1L)).thenReturn(true);
        when(withdrawalRepository.save(any())).thenAnswer(invocation -> {
            EpiWithdrawal arg = invocation.getArgument(0);
            return new EpiWithdrawal(1L, arg.getEpiId(), arg.getEmployeeId(), arg.getQuantity(), arg.getWithdrawnAt());
        });

        EpiWithdrawalResponse response = service.create(new EpiWithdrawalRequest(1L, 1L, 5));

        assertThat(response.getQuantity()).isEqualTo(5);
        assertThat(epi.getCurrentStock()).isEqualTo(45);
    }

    @Test
    void givenMissingEmployee_whenCreate_thenThrowsEmployeeNotFound() {
        Epi epi = new Epi(1L, "Luva", "desc", "url", 1L, 50, LocalDateTime.now());
        when(epiRepository.findById(1L)).thenReturn(Optional.of(epi));
        when(employeeClient.exists(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.create(new EpiWithdrawalRequest(1L, 999L, 1)))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void givenMissingEpi_whenCreate_thenThrowsEpiNotFound() {
        when(epiRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(new EpiWithdrawalRequest(404L, 1L, 1)))
                .isInstanceOf(EpiNotFoundException.class);
    }

    @Test
    void givenQuantityAboveStock_whenCreate_thenThrowsInsufficientStock() {
        Epi epi = new Epi(1L, "Luva", "desc", "url", 1L, 3, LocalDateTime.now());
        when(epiRepository.findById(1L)).thenReturn(Optional.of(epi));
        when(employeeClient.exists(1L)).thenReturn(true);

        assertThatThrownBy(() -> service.create(new EpiWithdrawalRequest(1L, 1L, 10)))
                .isInstanceOf(InsufficientStockException.class);
    }
}
