package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.dtos.OperacaoEpiRequest;
import br.com.renan.almoxarifado.dtos.OperacaoEpiResponse;
import br.com.renan.almoxarifado.dtos.OperacaoRequest;
import br.com.renan.almoxarifado.dtos.OperacaoResponse;
import br.com.renan.almoxarifado.dtos.OperacaoRiscoRequest;
import br.com.renan.almoxarifado.dtos.OperacaoRiscoResponse;
import br.com.renan.almoxarifado.services.OperacaoEpiService;
import br.com.renan.almoxarifado.services.OperacaoRiscoService;
import br.com.renan.almoxarifado.services.OperacaoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operacoes")
@RequiredArgsConstructor
public class OperacaoController {

    private final OperacaoService service;
    private final OperacaoEpiService operacaoEpiService;
    private final OperacaoRiscoService operacaoRiscoService;

    @PostMapping
    public ResponseEntity<OperacaoResponse> create(@Valid @RequestBody OperacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<OperacaoResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public OperacaoResponse findById(@PathVariable long id) {
        return service.findById(id);
    }

    @PostMapping("/{id}/epis")
    public ResponseEntity<OperacaoEpiResponse> addEpi(@PathVariable long id,
                                                        @Valid @RequestBody OperacaoEpiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operacaoEpiService.create(id, request));
    }

    @GetMapping("/{id}/epis")
    public List<OperacaoEpiResponse> findEpis(@PathVariable long id) {
        return operacaoEpiService.findByOperacaoId(id);
    }

    @PostMapping("/{id}/riscos")
    public ResponseEntity<OperacaoRiscoResponse> addRisco(@PathVariable long id,
                                                            @Valid @RequestBody OperacaoRiscoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operacaoRiscoService.create(id, request));
    }

    @GetMapping("/{id}/riscos")
    public List<OperacaoRiscoResponse> findRiscos(@PathVariable long id) {
        return operacaoRiscoService.findByOperacaoId(id);
    }
}
