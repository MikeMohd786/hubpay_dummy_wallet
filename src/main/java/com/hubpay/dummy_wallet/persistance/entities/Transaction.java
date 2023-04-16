package com.hubpay.dummy_wallet.persistance.entities;

import com.hubpay.dummy_wallet.models.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTION")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Wallet wallet;

    @Column(nullable = false, name = "AMOUNT")
    private BigDecimal amount;

    @Column(nullable = false, name = "TRANSACTION_TYPE")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false, name = "TRANSACTION_TIME")
    private LocalDateTime transactionTime;

}


