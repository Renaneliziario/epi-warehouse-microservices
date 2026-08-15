package br.com.renan.almoxarifado.services;

import br.com.renan.almoxarifado.dtos.OperacaoRiscoRequest;
import br.com.renan.almoxarifado.dtos.OperacaoRiscoResponse;
import br.com.renan.almoxarifado.entities.OperacaoRisco;
import br.com.renan.almoxarifado.repositories.OperacaoRiscoRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperacaoRiscoService {

    private static final Logger log = LoggerFactory.getLogger(OperacaoRiscoService.class);

    private final OperacaoRiscoRepository repository;
    private final OperacaoService operacaoService;
    private final RiscoService riscoService;

    public OperacaoRiscoResponse create(long operacaoId, OperacaoRiscoRequest request) {
        log.info("Associando risco a operacao, operacaoId={}, riscoId={}", operacaoId, request.getRiscoId());
        operacaoService.findEntity(operacaoId);
        riscoService.findEntity(request.getRiscoId());
        OperacaoRisco operacaoRisco = new OperacaoRisco(null, operacaoId, request.getRiscoId(),
                request.getNivelRisco(), request.getObservacao());
        OperacaoRisco saved = repository.save(operacaoRisco);
        log.info("Risco associado, id={}", saved.getId());
        return OperacaoRiscoResponse.from(saved);
    }

    public List<OperacaoRiscoResponse> findByOperacaoId(long operacaoId) {
        operacaoService.findEntity(operacaoId);
        List<OperacaoRisco> vinculos = repository.findByOperacaoId(operacaoId);
        List<OperacaoRiscoResponse> responses = new ArrayList<>();
        for (OperacaoRisco vinculo : vinculos) {
            responses.add(OperacaoRiscoResponse.from(vinculo));
        }
        return responses;
    }
}
