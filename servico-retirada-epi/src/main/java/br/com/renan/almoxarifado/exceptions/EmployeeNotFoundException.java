package br.com.renan.almoxarifado.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(long id) {
        super("Colaborador nao encontrado no servico de funcionarios: id " + id);
    }
}
