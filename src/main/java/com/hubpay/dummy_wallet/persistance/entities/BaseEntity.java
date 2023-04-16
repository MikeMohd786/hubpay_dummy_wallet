package com.hubpay.dummy_wallet.persistance.entities;

import javax.persistence.Column;
import java.time.LocalDateTime;


public class BaseEntity {

    @Column(name = "CREATED_AT")
    protected LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    protected LocalDateTime updatedAt;
}
