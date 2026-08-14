package br.com.renan.almoxarifado.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(long id) {
        super("Colaborador não encontrado: id " + id);
    }
}
