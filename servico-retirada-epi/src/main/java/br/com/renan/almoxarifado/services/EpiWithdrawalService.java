package br.com.renan.almoxarifado.services;

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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EpiWithdrawalService {

    private static final Logger log = LoggerFactory.getLogger(EpiWithdrawalService.class);

    private final EpiWithdrawalRepository withdrawalRepository;
    private final EpiRepository epiRepository;
    private final EmployeeClient employeeClient;

    @Transactional
    public EpiWithdrawalResponse create(EpiWithdrawalRequest request) {
        log.info("Registrando retirada, epiId={}, employeeId={}, quantity={}",
                request.getEpiId(), request.getEmployeeId(), request.getQuantity());

        Epi epi = epiRepository.findById(request.getEpiId())
                .orElseThrow(() -> {
                    log.warn("EPI inexistente ao registrar retirada, epiId={}", request.getEpiId());
                    return new EpiNotFoundException(request.getEpiId());
                });

        if (!employeeClient.exists(request.getEmployeeId())) {
            log.warn("Colaborador inexistente ao registrar retirada, employeeId={}", request.getEmployeeId());
            throw new EmployeeNotFoundException(request.getEmployeeId());
        }

        if (epi.getCurrentStock() < request.getQuantity()) {
            log.warn("Estoque insuficiente, epiId={}, solicitado={}, disponivel={}",
                    request.getEpiId(), request.getQuantity(), epi.getCurrentStock());
            throw new InsufficientStockException(request.getEpiId(), request.getQuantity(), epi.getCurrentStock());
        }

        epi.setCurrentStock(epi.getCurrentStock() - request.getQuantity());
        epiRepository.save(epi);

        EpiWithdrawal withdrawal = new EpiWithdrawal(null, request.getEpiId(), request.getEmployeeId(),
                request.getQuantity(), LocalDateTime.now());
        EpiWithdrawal saved = withdrawalRepository.save(withdrawal);
        log.info("Retirada registrada, id={}, estoque restante={}", saved.getId(), epi.getCurrentStock());
        return EpiWithdrawalResponse.from(saved);
    }

    public List<EpiWithdrawalResponse> findAll() {
        List<EpiWithdrawal> withdrawals = withdrawalRepository.findAll();
        List<EpiWithdrawalResponse> responses = new ArrayList<>();
        for (EpiWithdrawal withdrawal : withdrawals) {
            responses.add(EpiWithdrawalResponse.from(withdrawal));
        }
        return responses;
    }
}
