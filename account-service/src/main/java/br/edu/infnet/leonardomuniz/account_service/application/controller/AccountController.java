package br.edu.infnet.leonardomuniz.account_service.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.leonardomuniz.account_service.application.dto.AccountResponse;
import br.edu.infnet.leonardomuniz.account_service.domain.model.Account;
import br.edu.infnet.leonardomuniz.account_service.domain.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable Long id) {
        Account account = service.getById(id);

        return AccountResponse.builder()
                .id(account.getId())
                .ownerName(account.getOwnerName())
                .balance(account.getBalance())
                .build();
    }
}
