package br.edu.infnet.leonardomuniz.transaction.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Transaction {
    String id;
    Long accountId;
    Double amount;
    String status;
}
