package com.hubpay.dummy_wallet.persistance.entities;

import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "WALLET")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "BALANCE")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    public <E> Wallet(Long id, String customerName, BigDecimal balance) {
        this.id = id;
        this.customerName = customerName;
        this.balance = balance;
    }
}
