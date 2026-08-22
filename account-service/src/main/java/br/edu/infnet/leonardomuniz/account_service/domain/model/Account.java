package br.edu.infnet.leonardomuniz.account_service.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Account {
    Long id;
    String ownerName;
    Double balance;
}
