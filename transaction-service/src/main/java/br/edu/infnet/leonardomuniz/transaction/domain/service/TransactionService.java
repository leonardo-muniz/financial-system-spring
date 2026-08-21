package br.edu.infnet.leonardomuniz.transaction.domain.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.edu.infnet.leonardomuniz.transaction.application.dto.AccountDto;
import br.edu.infnet.leonardomuniz.transaction.domain.exception.InsufficientBalanceException;
import br.edu.infnet.leonardomuniz.transaction.domain.model.Transaction;
import br.edu.infnet.leonardomuniz.transaction.infrastructure.client.AccountClient;

@Service
public class TransactionService {

    private final AccountClient accountClient;

    public TransactionService(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public Transaction createForAccount(Long accountId, Double amount) {

        AccountDto account = accountClient.getAccount(accountId);

        if (account.getBalance() < amount) throw new InsufficientBalanceException(account.getBalance(), amount);

        return Transaction.builder()
                .id(UUID.randomUUID().toString())
                .accountId(account.getId())
                .amount(amount)
                .status("SUCCESS")
                .build();
    }
}
