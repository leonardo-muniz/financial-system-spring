package br.edu.infnet.leonardomuniz.transaction.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.edu.infnet.leonardomuniz.transaction.application.dto.AccountDto;

@FeignClient(name = "account-service")
public interface AccountClient {
    @GetMapping("api/accounts/{id}")
    AccountDto getAccount(@PathVariable("id") Long id);
}
