package br.edu.infnet.leonardomuniz.transaction.domain.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Double balance, Double amount) {
        super("Insufficient balance. Current: " + balance + ", required: " + amount);
    }
}
