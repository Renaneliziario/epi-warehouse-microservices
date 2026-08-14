package br.com.renan.almoxarifado.epiwithdrawal;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(long epiId, int requested, int available) {
        super("Estoque insuficiente para EPI id " + epiId + ": solicitado " + requested + ", disponivel " + available);
    }
}
