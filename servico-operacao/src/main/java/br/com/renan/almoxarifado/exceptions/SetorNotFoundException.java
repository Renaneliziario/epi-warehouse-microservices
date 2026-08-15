package br.com.renan.almoxarifado.exceptions;

public class SetorNotFoundException extends RuntimeException {

    public SetorNotFoundException(long id) {
        super("Setor não encontrado: id " + id);
    }
}
