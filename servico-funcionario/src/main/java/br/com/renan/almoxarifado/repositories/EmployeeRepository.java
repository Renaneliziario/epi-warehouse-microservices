package br.com.renan.almoxarifado.repositories;

import br.com.renan.almoxarifado.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);
}
