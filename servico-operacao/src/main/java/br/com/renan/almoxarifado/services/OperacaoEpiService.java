package br.com.renan.almoxarifado.services;

import br.com.renan.almoxarifado.clients.EpiClient;
import br.com.renan.almoxarifado.clients.EpiInfo;
import br.com.renan.almoxarifado.dtos.OperacaoEpiRequest;
import br.com.renan.almoxarifado.dtos.OperacaoEpiResponse;
import br.com.renan.almoxarifado.entities.OperacaoEpi;
import br.com.renan.almoxarifado.exceptions.EpiNotFoundException;
import br.com.renan.almoxarifado.repositories.OperacaoEpiRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperacaoEpiService {

    private static final Logger log = LoggerFactory.getLogger(OperacaoEpiService.class);

    private final OperacaoEpiRepository repository;
    private final OperacaoService operacaoService;
    private final EpiClient epiClient;

    public OperacaoEpiResponse create(long operacaoId, OperacaoEpiRequest request) {
        log.info("Associando EPI a operação, operacaoId={}, epiId={}", operacaoId, request.getEpiId());
        operacaoService.findEntity(operacaoId);
        EpiInfo epiInfo = buscarEpiOuFalhar(request.getEpiId());
        OperacaoEpi operacaoEpi = new OperacaoEpi(null, operacaoId, request.getEpiId(), request.isObrigatorio(),
                request.getObservacao());
        OperacaoEpi saved = repository.save(operacaoEpi);
        log.info("EPI associado, id={}", saved.getId());
        return OperacaoEpiResponse.from(saved, epiInfo.getName());
    }

    public List<OperacaoEpiResponse> findByOperacaoId(long operacaoId) {
        operacaoService.findEntity(operacaoId);
        List<OperacaoEpi> vinculos = repository.findByOperacaoId(operacaoId);
        List<OperacaoEpiResponse> responses = new ArrayList<>();
        for (OperacaoEpi vinculo : vinculos) {
            EpiInfo epiInfo = epiClient.fetch(vinculo.getEpiId());
            String nome = epiInfo != null ? epiInfo.getName() : null;
            responses.add(OperacaoEpiResponse.from(vinculo, nome));
        }
        return responses;
    }

    private EpiInfo buscarEpiOuFalhar(long epiId) {
        if (!epiClient.exists(epiId)) {
            log.warn("EPI inexistente ao associar à operação, epiId={}", epiId);
            throw new EpiNotFoundException(epiId);
        }
        return epiClient.fetch(epiId);
    }
}
