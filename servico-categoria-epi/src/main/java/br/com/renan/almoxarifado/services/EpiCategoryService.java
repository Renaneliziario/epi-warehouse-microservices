package br.com.renan.almoxarifado.services;

import br.com.renan.almoxarifado.dtos.EpiCategoryRequest;
import br.com.renan.almoxarifado.dtos.EpiCategoryResponse;
import br.com.renan.almoxarifado.entities.EpiCategory;
import br.com.renan.almoxarifado.exceptions.EpiCategoryNotFoundException;
import br.com.renan.almoxarifado.repositories.EpiCategoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EpiCategoryService {

    private static final Logger log = LoggerFactory.getLogger(EpiCategoryService.class);

    private final EpiCategoryRepository repository;

    public EpiCategoryResponse create(EpiCategoryRequest request) {
        log.info("Cadastrando categoria de EPI, nome={}", request.getName());
        EpiCategory category = new EpiCategory(null, request.getName(), request.getDescription(), LocalDateTime.now());
        EpiCategory saved = repository.save(category);
        log.info("Categoria cadastrada, id={}", saved.getId());
        return EpiCategoryResponse.from(saved);
    }

    public List<EpiCategoryResponse> findAll() {
        return repository.findAll().stream().map(EpiCategoryResponse::from).toList();
    }

    public EpiCategoryResponse findById(long id) {
        EpiCategory category = repository.findById(id)
                .orElseThrow(() -> new EpiCategoryNotFoundException(id));
        return EpiCategoryResponse.from(category);
    }

    public boolean exists(long id) {
        return repository.existsById(id);
    }
}
