package br.com.renan.almoxarifado.epiwithdrawal;

public class EpiNotFoundException extends RuntimeException {

    public EpiNotFoundException(long id) {
        super("EPI nao encontrado no catalogo: id " + id);
    }
}
