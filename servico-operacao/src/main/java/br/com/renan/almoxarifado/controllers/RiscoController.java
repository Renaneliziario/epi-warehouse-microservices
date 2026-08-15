package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.dtos.RiscoRequest;
import br.com.renan.almoxarifado.dtos.RiscoResponse;
import br.com.renan.almoxarifado.services.RiscoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riscos")
@RequiredArgsConstructor
public class RiscoController {

    private final RiscoService service;

    @PostMapping
    public ResponseEntity<RiscoResponse> create(@Valid @RequestBody RiscoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<RiscoResponse> findAll() {
        return service.findAll();
    }
}
