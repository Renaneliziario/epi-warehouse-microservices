package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.dtos.EpiWithdrawalRequest;
import br.com.renan.almoxarifado.dtos.EpiWithdrawalResponse;
import br.com.renan.almoxarifado.services.EpiWithdrawalService;
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
@RequestMapping("/retiradas")
@RequiredArgsConstructor
public class EpiWithdrawalController {

    private final EpiWithdrawalService service;

    @PostMapping
    public ResponseEntity<EpiWithdrawalResponse> create(@Valid @RequestBody EpiWithdrawalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<EpiWithdrawalResponse> findAll() {
        return service.findAll();
    }
}