package br.com.renan.almoxarifado.exceptions;

public class RiscoNotFoundException extends RuntimeException {

    public RiscoNotFoundException(long id) {
        super("Risco não encontrado: id " + id);
    }
}
