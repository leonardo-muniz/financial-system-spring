package br.edu.infnet.leonardomuniz.transaction.domain.exception;

public class MaxTransactionAmountExceededException extends RuntimeException {

    public MaxTransactionAmountExceededException(Double amount, Double limit) {
        super("Transaction amount " + amount + " exceeds the maximum allowed: " + limit);
    }
}
