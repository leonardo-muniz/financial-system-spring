package br.edu.infnet.leonardomuniz.transaction.domain.exception;

public class InvalidTransactionTypeException extends RuntimeException {
    public InvalidTransactionTypeException(String type) {
        super("Invalid transaction type: " + type);
    }
}
