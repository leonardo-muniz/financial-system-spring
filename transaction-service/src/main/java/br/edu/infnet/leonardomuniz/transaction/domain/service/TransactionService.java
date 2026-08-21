package br.edu.infnet.leonardomuniz.transaction.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.infnet.leonardomuniz.transaction.application.dto.AccountDto;
import br.edu.infnet.leonardomuniz.transaction.application.dto.TransactionResponse;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.InsufficientBalanceException;
import br.edu.infnet.leonardomuniz.transaction.domain.model.Transaction;
import br.edu.infnet.leonardomuniz.transaction.infrastructure.client.AccountClient;

@Service
public class TransactionService {

    private final AccountClient accountClient;

    public TransactionService(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public TransactionResponse createForAccount(Long accountId, Double amount) {

        AccountDto account = accountClient.getAccount(accountId);

        if (account.getBalance() < amount) throw new InsufficientBalanceException(account.getBalance(), amount);

        Transaction tx = Transaction.builder()
            .id(UUID.randomUUID().toString())
            .accountId(accountId)
            .amount(amount)
            .status("SUCCESS")
            .build();

        return TransactionResponse.builder()
                .transactionId(tx.getId())
                .accountId(tx.getAccountId())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .account(account)
                .build();
    }
}
