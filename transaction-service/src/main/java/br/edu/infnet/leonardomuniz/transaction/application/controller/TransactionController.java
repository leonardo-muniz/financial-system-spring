package br.edu.infnet.leonardomuniz.transaction.application.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.leonardomuniz.transaction.application.dto.TransactionResponse;
import br.edu.infnet.leonardomuniz.transaction.domain.service.TransactionService;

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
        return service.createForAccount(accountId, amount);
    }
}
