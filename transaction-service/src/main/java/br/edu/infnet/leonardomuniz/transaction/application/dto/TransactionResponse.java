package br.edu.infnet.leonardomuniz.transaction.application.dto;

import br.edu.infnet.leonardomuniz.transaction.domain.model.TransactionType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionResponse {
    String transactionId;
    Long accountId;
    Double amount;
    TransactionType type;
    String status;
    AccountDto account;
}
