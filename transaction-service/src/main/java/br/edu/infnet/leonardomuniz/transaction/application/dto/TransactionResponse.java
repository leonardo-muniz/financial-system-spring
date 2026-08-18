package br.edu.infnet.leonardomuniz.transaction.application.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionResponse {
    String transactionId;
    Long accountId;
    Double amount;
    String status;
    AccountDto account;
}
