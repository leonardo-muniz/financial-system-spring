package br.edu.infnet.leonardomuniz.transaction.domain.exception;

public class AccountClosedException extends RuntimeException {

    public AccountClosedException(Long accountId) {
        super("Account " + accountId + " is closed");
    }
}
