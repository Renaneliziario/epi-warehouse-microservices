package br.com.renan.almoxarifado.exceptions;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(long epiId, int requested, int available) {
        super("Estoque insuficiente para EPI id " + epiId + ": solicitado " + requested + ", disponível " + available);
    }
}
