package br.com.renan.almoxarifado.services;

import br.com.renan.almoxarifado.dtos.OperacaoRequest;
import br.com.renan.almoxarifado.dtos.OperacaoResponse;
import br.com.renan.almoxarifado.entities.Operacao;
import br.com.renan.almoxarifado.exceptions.OperacaoNotFoundException;
import br.com.renan.almoxarifado.exceptions.SetorNotFoundException;
import br.com.renan.almoxarifado.repositories.OperacaoRepository;
import br.com.renan.almoxarifado.repositories.SetorRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    private static final Logger log = LoggerFactory.getLogger(OperacaoService.class);

    private final OperacaoRepository repository;
    private final SetorRepository setorRepository;

    public OperacaoResponse create(OperacaoRequest request) {
        log.info("Cadastrando operação, nome={}, setorId={}", request.getNome(), request.getSetorId());
        if (!setorRepository.existsById(request.getSetorId())) {
            log.warn("Setor inexistente ao cadastrar operação, setorId={}", request.getSetorId());
            throw new SetorNotFoundException(request.getSetorId());
        }
        Operacao operacao = new Operacao(null, request.getNome(), request.getDescricao(), request.getSetorId(),
                LocalDateTime.now());
        Operacao saved = repository.save(operacao);
        log.info("Operação cadastrada, id={}", saved.getId());
        return OperacaoResponse.from(saved);
    }

    public List<OperacaoResponse> findAll() {
        List<Operacao> operacoes = repository.findAll();
        List<OperacaoResponse> responses = new ArrayList<>();
        for (Operacao operacao : operacoes) {
            responses.add(OperacaoResponse.from(operacao));
        }
        return responses;
    }

    public OperacaoResponse findById(long id) {
        return OperacaoResponse.from(findEntity(id));
    }

    Operacao findEntity(long id) {
        return repository.findById(id).orElseThrow(() -> new OperacaoNotFoundException(id));
    }
}
