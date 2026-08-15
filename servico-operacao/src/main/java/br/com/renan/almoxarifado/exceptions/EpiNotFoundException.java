package br.com.renan.almoxarifado.exceptions;

public class EpiNotFoundException extends RuntimeException {

    public EpiNotFoundException(long id) {
        super("EPI não encontrado no catálogo: id " + id);
    }
}
