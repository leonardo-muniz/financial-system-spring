package br.edu.infnet.leonardomuniz.account_service.domain.service;

import org.springframework.stereotype.Service;

import br.edu.infnet.leonardomuniz.account_service.domain.model.Account;

@Service
public class AccountService {
    public Account getById(Long id) {
        return Account.builder()
                .id(id)
                .ownerName("Leonardo")
                .balance(1000.0)
                .build();
    }
}
