package br.com.renan.almoxarifado.exceptions;

public class OperacaoNotFoundException extends RuntimeException {

    public OperacaoNotFoundException(long id) {
        super("Operação não encontrada: id " + id);
    }
}
