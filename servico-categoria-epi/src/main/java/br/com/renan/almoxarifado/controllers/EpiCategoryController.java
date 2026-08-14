package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.dtos.EpiCategoryRequest;
import br.com.renan.almoxarifado.dtos.EpiCategoryResponse;
import br.com.renan.almoxarifado.services.EpiCategoryService;
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
@RequestMapping("/epi-categories")
@RequiredArgsConstructor
public class EpiCategoryController {

    private final EpiCategoryService service;

    @PostMapping
    public ResponseEntity<EpiCategoryResponse> create(@Valid @RequestBody EpiCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<EpiCategoryResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EpiCategoryResponse findById(@PathVariable long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> exists(@PathVariable long id) {
        return service.exists(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}