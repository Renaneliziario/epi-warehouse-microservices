package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.dtos.EpiRequest;
import br.com.renan.almoxarifado.dtos.EpiResponse;
import br.com.renan.almoxarifado.services.EpiService;
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
@RequestMapping("/epis")
@RequiredArgsConstructor
public class EpiController {

    private final EpiService service;

    @PostMapping
    public ResponseEntity<EpiResponse> create(@Valid @RequestBody EpiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<EpiResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EpiResponse findById(@PathVariable long id) {
        return service.findById(id);
    }

    @GetMapping("/dia")
    public EpiResponse epiDoDia() {
        return service.randomOfTheDay();
    }
}