package br.com.renan.almoxarifado.exceptions;

public class OperacaoNotFoundException extends RuntimeException {

    public OperacaoNotFoundException(long id) {
        super("Operacao nao encontrada: id " + id);
    }
}
