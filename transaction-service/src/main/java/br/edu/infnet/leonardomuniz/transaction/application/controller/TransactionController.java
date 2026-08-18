package br.edu.infnet.leonardomuniz.transaction.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.leonardomuniz.transaction.application.dto.TransactionResponse;
import br.edu.infnet.leonardomuniz.transaction.domain.model.Transaction;
import br.edu.infnet.leonardomuniz.transaction.domain.service.TransactionService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/{accountId}")
    public TransactionResponse create(
            @PathVariable Long accountId,
            @RequestBody Double amount
    ) {
        Transaction tx = service.createForAccount(accountId, amount);
        
        return TransactionResponse.builder()
                .transactionId(tx.getId())
                .accountId(tx.getAccountId())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .build();
    }
}
