package br.com.renan.almoxarifado.epiwithdrawal;

public class EpiCategoryNotFoundException extends RuntimeException {

    public EpiCategoryNotFoundException(long id) {
        super("Categoria de EPI nao encontrada no servico de categorias: id " + id);
    }
}
