package br.edu.infnet.leonardomuniz.transaction.domain.exception;

public class DailyLimitExceededException extends RuntimeException {

    public DailyLimitExceededException(Double total, Double limit) {
        super("Daily transaction limit exceeded: " + total + " / " + limit);
    }
}
