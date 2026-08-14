package br.com.renan.almoxarifado.employee;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(long id) {
        super("Colaborador não encontrado: id " + id);
    }
}
