package br.edu.infnet.leonardomuniz.account_service.domain.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import br.edu.infnet.leonardomuniz.account_service.domain.model.Account;
import br.edu.infnet.leonardomuniz.account_service.domain.exception.AccountNotFoundException;

@Service
public class AccountService {

    private final Map<Long, Account> fakeDb = Map.of(
            1L, Account.builder().id(1L).ownerName("Leonardo").balance(1000.0).build(),
            2L, Account.builder().id(2L).ownerName("Maria").balance(500.0).build()
    );

    public Account getById(Long id) { return fakeDb.getOrDefault(id, null); }

    public void validateAccountExists(Long id) {
        if (!fakeDb.containsKey(id)) throw new AccountNotFoundException(id);
    }
    
}
