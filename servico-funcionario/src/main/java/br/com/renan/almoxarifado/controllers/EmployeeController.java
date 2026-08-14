package br.com.renan.almoxarifado.controllers;

import br.com.renan.almoxarifado.dtos.EmployeeRequest;
import br.com.renan.almoxarifado.dtos.EmployeeResponse;
import br.com.renan.almoxarifado.services.EmployeeService;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<EmployeeResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeResponse findById(@PathVariable long id) {
        return service.findById(id);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> exists(@PathVariable long id) {
        return service.exists(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}