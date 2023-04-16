package com.hubpay.dummy_wallet.services;

import com.hubpay.dummy_wallet.exceptions.InsufficientFundsException;
import com.hubpay.dummy_wallet.exceptions.InvalidAmountException;
import com.hubpay.dummy_wallet.exceptions.WalletNotFoundException;
import com.hubpay.dummy_wallet.models.TransactionType;
import com.hubpay.dummy_wallet.persistance.entities.Transaction;
import com.hubpay.dummy_wallet.persistance.entities.Wallet;
import com.hubpay.dummy_wallet.persistance.repositories.TransactionRepository;
import com.hubpay.dummy_wallet.persistance.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository,
                         TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void  addFunds(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount: " + amount);
        }

        if (amount.compareTo(BigDecimal.valueOf(10000)) > 0) {
            throw new InvalidAmountException("Amount exceeds maximum: " + amount);
        }

        if (amount.compareTo(BigDecimal.valueOf(10)) < 0) {
            throw new InvalidAmountException("Amount below minimum: " + amount);
        }
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        logTransaction(wallet, amount, TransactionType.CREDIT, LocalDateTime.now());
    }

    @Transactional
    public Wallet withdrawFunds(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount: " + amount);
        }

        if (amount.compareTo(BigDecimal.valueOf(5000)) > 0) {
            throw new InvalidAmountException("Amount exceeds maximum: " + amount);
        }

        if (amount.compareTo(wallet.getBalance()) > 0) {
            throw new InsufficientFundsException("Insufficient funds: " + wallet.getBalance());
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        Wallet updatedWallet = walletRepository.save(wallet);
        logTransaction(updatedWallet, amount, TransactionType.DEBIT, LocalDateTime.now());
        return updatedWallet;
    }

    public Page<Transaction> getTransactions(Long walletId, int page, int size) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Digital wallet not found for wallet ID: " + walletId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return transactionRepository.findByWallet(wallet, pageable);
    }


    private void logTransaction(Wallet wallet, BigDecimal amount, TransactionType transactionType, LocalDateTime transactionTime) {
        transactionRepository.save(Transaction.builder()
                .type(transactionType)
                .amount(amount)
                .transactionTime(transactionTime)
                .wallet(wallet)
                .build());
    }

}
