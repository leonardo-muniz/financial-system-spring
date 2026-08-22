package br.edu.infnet.leonardomuniz.transaction.domain.exception;

public class AccountBlockedException extends RuntimeException {

    public AccountBlockedException(Long accountId) {
        super("Account " + accountId + " is blocked");
    }
}
