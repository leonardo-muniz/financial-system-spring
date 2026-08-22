package br.edu.infnet.leonardomuniz.transaction.domain.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.infnet.leonardomuniz.transaction.application.dto.AccountDto;
import br.edu.infnet.leonardomuniz.transaction.application.dto.TransactionResponse;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.AccountBlockedException;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.AccountClosedException;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.DailyLimitExceededException;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.InsufficientBalanceException;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.InvalidTransactionTypeException;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.MaxTransactionAmountExceededException;
import br.edu.infnet.leonardomuniz.transaction.domain.model.Transaction;
import br.edu.infnet.leonardomuniz.transaction.domain.model.TransactionType;
import br.edu.infnet.leonardomuniz.transaction.infrastructure.client.AccountClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class TransactionService {

    private final AccountClient accountClient;

    // Limites
    private static final double MAX_TRANSACTION_AMOUNT = 2000.0;
    private static final double DAILY_LIMIT = 5000.0;

    // Controle simples de limite diário
    private final Map<Long, Double> dailyTotals = new HashMap<>();

    public TransactionService(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackAccount")
    @Retry(name = "accountService")
    public TransactionResponse createForAccount(Long accountId, Double amount, TransactionType type) {

        AccountDto account = accountClient.getAccount(accountId);

        validateAccountStatus(account);
        validateTransactionType(type);
        validateMaxAmount(amount);
        validateDailyLimit(accountId, amount);

        if (type == TransactionType.DEBIT && account.getBalance() < amount) throw new InsufficientBalanceException(account.getBalance(), amount);

        String status = determineStatus(amount);

        Transaction tx = Transaction.builder()
            .id(UUID.randomUUID().toString())
            .accountId(accountId)
            .amount(amount)
            .type(type)
            .status(status)
            .build();

        updateDailyTotal(accountId, amount);

        return TransactionResponse.builder()
                .transactionId(tx.getId())
                .accountId(tx.getAccountId())
                .amount(tx.getAmount())
                .type(tx.getType())
                .status(tx.getStatus())
                .account(account)
                .build();
    }

    private void validateAccountStatus(AccountDto account) {
        if ("BLOCKED".equalsIgnoreCase(account.getStatus()))
            throw new AccountBlockedException(account.getId());

        if ("CLOSED".equalsIgnoreCase(account.getStatus()))
            throw new AccountClosedException(account.getId());

    }

    private void validateTransactionType(TransactionType type) {
        if (type == null)
            throw new InvalidTransactionTypeException("null");
    }

    private void validateMaxAmount(Double amount) {
        if (amount > MAX_TRANSACTION_AMOUNT)
            throw new MaxTransactionAmountExceededException(amount, MAX_TRANSACTION_AMOUNT);
    }

    private void validateDailyLimit(Long accountId, Double amount) {
        double used = dailyTotals.getOrDefault(accountId, 0.0);
        if (used + amount > DAILY_LIMIT)
            throw new DailyLimitExceededException(amount, DAILY_LIMIT - used);
    }

    private void updateDailyTotal(Long accountId, Double amount) {
        dailyTotals.put(accountId, dailyTotals.getOrDefault(accountId, 0.0) + amount);
    }

    private String determineStatus(Double amount) {
        return amount > 1000.0 ? "REVIEW" : "SUCCESS";
    }

    // Fallback chamado quando o Circuit Breaker abre ou o Retry esgota
    public TransactionResponse fallbackAccount(Long accountId, Double amount, TransactionType type, Throwable t) {

        return TransactionResponse.builder()
                .transactionId("N/A")
                .accountId(accountId)
                .amount(amount)
                .type(type)
                .status("FAILED_ACCOUNT_SERVICE")
                .account(null)
                .build();
    }
}
