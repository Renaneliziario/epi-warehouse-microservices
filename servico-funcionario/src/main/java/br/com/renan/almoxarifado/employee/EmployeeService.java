package br.com.renan.almoxarifado.employee;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository repository;

    public EmployeeResponse create(EmployeeRequest request) {
        log.info("Cadastrando colaborador, email={}", request.getEmail());
        Employee employee = new Employee(null, request.getName(), request.getEmail(), LocalDateTime.now());
        Employee saved = repository.save(employee);
        log.info("Colaborador cadastrado, id={}", saved.getId());
        return EmployeeResponse.from(saved);
    }

    public List<EmployeeResponse> findAll() {
        return repository.findAll().stream().map(EmployeeResponse::from).toList();
    }

    public EmployeeResponse findById(long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Colaborador nao encontrado, id={}", id);
                    return new EmployeeNotFoundException(id);
                });
        return EmployeeResponse.from(employee);
    }

    public boolean exists(long id) {
        return repository.existsById(id);
    }
}