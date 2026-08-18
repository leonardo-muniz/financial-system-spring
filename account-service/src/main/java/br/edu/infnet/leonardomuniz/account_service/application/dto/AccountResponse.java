package br.edu.infnet.leonardomuniz.account_service.application.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountResponse {
    Long id;
    String ownerName;
    Double balance;
}
