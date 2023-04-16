package com.hubpay.dummy_wallet.services;

import com.hubpay.dummy_wallet.exceptions.InsufficientFundsException;
import com.hubpay.dummy_wallet.exceptions.InvalidAmountException;
import com.hubpay.dummy_wallet.exceptions.WalletNotFoundException;
import com.hubpay.dummy_wallet.models.TransactionDTO;
import com.hubpay.dummy_wallet.models.TransactionPage;
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
import java.util.List;
import java.util.stream.Collectors;

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
        fundCreditValidation(amount);
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        logTransaction(wallet, amount, TransactionType.CREDIT, LocalDateTime.now());
    }

    @Transactional
    public Wallet withdrawFunds(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));
        fundDebitValidation(amount, wallet);
        wallet.setBalance(wallet.getBalance().subtract(amount));
        Wallet updatedWallet = walletRepository.save(wallet);
        logTransaction(updatedWallet, amount, TransactionType.DEBIT, LocalDateTime.now());
        return updatedWallet;
    }

    public TransactionPage getTransactions(Long walletId, int page, int size) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Digital wallet not found for wallet ID: " + walletId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionTime").descending());
        Page transactions = transactionRepository.findByWallet(wallet, pageable);
        return TransactionPage.builder()
                .page(transactions.getNumber())
                .size(transactions.getSize())
                .totalPage(transactions.getTotalPages())
                .transactions((List<TransactionDTO>) transactions.getContent().stream().map(e -> TransactionDTO.builder()
                            .id(((Transaction) e).getId())
                            .createdAt(((Transaction) e).getCreatedAt())
                            .transactionTime(((Transaction) e).getTransactionTime())
                            .updatedAt(((Transaction) e).getUpdatedAt())
                            .type(((Transaction) e).getType())
                            .amount(((Transaction) e).getAmount())
                        .build()).collect(Collectors.toList()))
                .build();
    }


    private void logTransaction(Wallet wallet, BigDecimal amount, TransactionType transactionType, LocalDateTime transactionTime) {
        transactionRepository.save(Transaction.builder()
                .type(transactionType)
                .amount(amount)
                .transactionTime(transactionTime)
                .wallet(wallet)
                .build());
    }

    private void fundCreditValidation( BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount: " + amount);
        }

        if (amount.compareTo(BigDecimal.valueOf(10000)) > 0) {
            throw new InvalidAmountException("Amount "+amount+" exceeds maximum credit limit 10000");
        }

        if (amount.compareTo(BigDecimal.valueOf(10)) < 0) {
            throw new InvalidAmountException("Amount "+amount+" below minimum credit limit 10");
        }
    }

    private void fundDebitValidation(BigDecimal amount, Wallet wallet) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount: " + amount);
        }

        if (amount.compareTo(BigDecimal.valueOf(5000)) > 0) {
            throw new InvalidAmountException("Amount "+amount+" exceeds maximum withdrawal limit 5000");
        }

        if (amount.compareTo(wallet.getBalance()) > 0) {
            throw new InsufficientFundsException("Insufficient funds: " + wallet.getBalance());
        }
    }

}
