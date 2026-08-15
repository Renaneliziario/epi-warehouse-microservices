package br.com.renan.almoxarifado.services;

import br.com.renan.almoxarifado.dtos.SetorRequest;
import br.com.renan.almoxarifado.dtos.SetorResponse;
import br.com.renan.almoxarifado.entities.Setor;
import br.com.renan.almoxarifado.exceptions.SetorNotFoundException;
import br.com.renan.almoxarifado.repositories.SetorRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SetorService {

    private static final Logger log = LoggerFactory.getLogger(SetorService.class);

    private final SetorRepository repository;

    public SetorResponse create(SetorRequest request) {
        log.info("Cadastrando setor, nome={}", request.getNome());
        Setor setor = new Setor(null, request.getNome(), request.getDescricao());
        Setor saved = repository.save(setor);
        log.info("Setor cadastrado, id={}", saved.getId());
        return SetorResponse.from(saved);
    }

    public List<SetorResponse> findAll() {
        List<Setor> setores = repository.findAll();
        List<SetorResponse> responses = new ArrayList<>();
        for (Setor setor : setores) {
            responses.add(SetorResponse.from(setor));
        }
        return responses;
    }

    Setor findEntity(long id) {
        return repository.findById(id).orElseThrow(() -> new SetorNotFoundException(id));
    }
}
