package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.dtos.SetorRequest;
import br.com.renan.almoxarifado.dtos.SetorResponse;
import br.com.renan.almoxarifado.services.SetorService;
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
@RequestMapping("/setores")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService service;

    @PostMapping
    public ResponseEntity<SetorResponse> create(@Valid @RequestBody SetorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<SetorResponse> findAll() {
        return service.findAll();
    }
}
