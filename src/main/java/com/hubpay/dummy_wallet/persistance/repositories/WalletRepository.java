package com.hubpay.dummy_wallet.persistance.repositories;

import com.hubpay.dummy_wallet.persistance.entities.Transaction;
import com.hubpay.dummy_wallet.persistance.entities.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
}