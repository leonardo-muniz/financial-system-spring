package br.edu.infnet.leonardomuniz.transaction.application.dto;

import lombok.Value;

@Value
public class AccountDto {
    Long id;
    String ownerName;
    Double balance;
}
