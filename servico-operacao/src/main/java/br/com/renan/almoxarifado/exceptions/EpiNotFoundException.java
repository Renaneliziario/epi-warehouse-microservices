package br.com.renan.almoxarifado.exceptions;

public class EpiNotFoundException extends RuntimeException {

    public EpiNotFoundException(long id) {
        super("EPI nao encontrado no catalogo: id " + id);
    }
}
