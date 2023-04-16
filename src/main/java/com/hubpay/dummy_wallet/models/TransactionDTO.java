package com.hubpay.dummy_wallet.models;

import com.hubpay.dummy_wallet.persistance.entities.Wallet;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime transactionTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
