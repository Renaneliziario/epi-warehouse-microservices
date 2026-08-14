package br.com.renan.almoxarifado.epicategory;

public class EpiCategoryNotFoundException extends RuntimeException {

    public EpiCategoryNotFoundException(long id) {
        super("Categoria de EPI nao encontrada: id " + id);
    }
}