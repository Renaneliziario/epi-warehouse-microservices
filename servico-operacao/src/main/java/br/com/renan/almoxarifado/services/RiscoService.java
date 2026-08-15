package br.com.renan.almoxarifado.services;

import br.com.renan.almoxarifado.dtos.RiscoRequest;
import br.com.renan.almoxarifado.dtos.RiscoResponse;
import br.com.renan.almoxarifado.entities.Risco;
import br.com.renan.almoxarifado.exceptions.RiscoNotFoundException;
import br.com.renan.almoxarifado.repositories.RiscoRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiscoService {

    private static final Logger log = LoggerFactory.getLogger(RiscoService.class);

    private final RiscoRepository repository;

    public RiscoResponse create(RiscoRequest request) {
        log.info("Cadastrando risco, nome={}", request.getNome());
        Risco risco = new Risco(null, request.getNome(), request.getDescricao(), request.getCategoria());
        Risco saved = repository.save(risco);
        log.info("Risco cadastrado, id={}", saved.getId());
        return RiscoResponse.from(saved);
    }

    public List<RiscoResponse> findAll() {
        List<Risco> riscos = repository.findAll();
        List<RiscoResponse> responses = new ArrayList<>();
        for (Risco risco : riscos) {
            responses.add(RiscoResponse.from(risco));
        }
        return responses;
    }

    Risco findEntity(long id) {
        return repository.findById(id).orElseThrow(() -> new RiscoNotFoundException(id));
    }
}
