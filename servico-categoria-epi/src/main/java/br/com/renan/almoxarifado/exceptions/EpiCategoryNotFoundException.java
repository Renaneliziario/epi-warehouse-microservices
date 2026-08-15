package br.com.renan.almoxarifado.exceptions;

public class EpiCategoryNotFoundException extends RuntimeException {

    public EpiCategoryNotFoundException(long id) {
        super("Categoria de EPI não encontrada: id " + id);
    }
}
