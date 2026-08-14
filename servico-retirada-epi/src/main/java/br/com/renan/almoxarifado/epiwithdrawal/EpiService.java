package br.com.renan.almoxarifado.epiwithdrawal;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EpiService {

    private static final Logger log = LoggerFactory.getLogger(EpiService.class);

    private final EpiRepository repository;
    private final EpiCategoryClient epiCategoryClient;

    public EpiResponse create(EpiRequest request) {
        log.info("Cadastrando EPI no catalogo, nome={}, epiCategoryId={}", request.getName(), request.getEpiCategoryId());
        if (!epiCategoryClient.exists(request.getEpiCategoryId())) {
            log.warn("Categoria inexistente ao cadastrar EPI, epiCategoryId={}", request.getEpiCategoryId());
            throw new EpiCategoryNotFoundException(request.getEpiCategoryId());
        }
        Epi epi = new Epi(null, request.getName(), request.getDescription(), request.getDocumentUrl(),
                request.getEpiCategoryId(), request.getCurrentStock(), LocalDateTime.now());
        Epi saved = repository.save(epi);
        log.info("EPI cadastrado, id={}", saved.getId());
        return EpiResponse.from(saved);
    }

    public List<EpiResponse> findAll() {
        return repository.findAll().stream().map(EpiResponse::from).toList();
    }

    public EpiResponse findById(long id) {
        return EpiResponse.from(findEntity(id));
    }

    public EpiResponse randomOfTheDay() {
        Epi epi = repository.findRandom().orElseThrow(() -> new EpiNotFoundException(0L));
        log.info("EPI do dia sorteado, id={}", epi.getId());
        return EpiResponse.from(epi);
    }

    Epi findEntity(long id) {
        return repository.findById(id).orElseThrow(() -> new EpiNotFoundException(id));
    }
}
