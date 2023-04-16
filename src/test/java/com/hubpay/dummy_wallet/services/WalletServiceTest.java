package com.hubpay.dummy_wallet.services;

import com.hubpay.dummy_wallet.exceptions.InsufficientFundsException;
import com.hubpay.dummy_wallet.exceptions.InvalidAmountException;
import com.hubpay.dummy_wallet.models.TransactionType;
import com.hubpay.dummy_wallet.persistance.entities.Transaction;
import com.hubpay.dummy_wallet.persistance.entities.Wallet;
import com.hubpay.dummy_wallet.persistance.repositories.TransactionRepository;
import com.hubpay.dummy_wallet.persistance.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository  transactionRepository;

    private WalletService walletService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        walletService = new WalletService(walletRepository, transactionRepository);
    }

    @Test
    public void testAddFunds() {

        Wallet wallet = new Wallet(1L, "Test Customer", BigDecimal.ZERO);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, wallet, new BigDecimal("100"), TransactionType.CREDIT, LocalDateTime.now()));

        when(walletRepository.findById(any())).thenReturn(java.util.Optional.of(wallet));
        when(walletRepository.save(any())).thenReturn(wallet);

        walletService.addFunds(1L, new BigDecimal("100"));

        verify(walletRepository, times(1)).save(any());
        verify(transactionRepository, times(1)).save(any());
        verify(walletRepository, times(1)).findById(any());
    }

    @Test
    public void testWithdrawFunds() {
        Wallet wallet = new Wallet(1L, "Test Customer", new BigDecimal("100"));
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, wallet, new BigDecimal("50"), TransactionType.DEBIT, LocalDateTime.now()));

        when(walletRepository.findById(any())).thenReturn(java.util.Optional.of(wallet));
        when(walletRepository.save(any())).thenReturn(wallet);

        walletService.withdrawFunds(1L, new BigDecimal("50"));

        verify(walletRepository, times(1)).save(any());
        verify(transactionRepository, times(1)).save(any());
        verify(walletRepository, times(1)).findById(any());
    }

    @Test
    public void testWithdrawFundsInsufficientFunds() {
        Wallet wallet = new Wallet(1L, "Test Customer", new BigDecimal("100"));

        when(walletRepository.findById(any())).thenReturn(java.util.Optional.of(wallet));

        assertThrows(InsufficientFundsException.class, () -> walletService.withdrawFunds(1L, new BigDecimal("150")));
        assertEquals(new BigDecimal("100"), wallet.getBalance());
    }


    @Test
    public void testWithdrawFundsExceedWithdrawalLimit() {
        Wallet wallet = new Wallet(1L, "Test Customer", new BigDecimal("100"));

        when(walletRepository.findById(any())).thenReturn(java.util.Optional.of(wallet));

        assertThrows(InvalidAmountException.class, () -> walletService.withdrawFunds(1L, new BigDecimal("5001")));
    }

    @Test
    public void testWithdrawInvalidAmount() {
        Wallet wallet = new Wallet(1L, "Test Customer", new BigDecimal("100"));

        when(walletRepository.findById(any())).thenReturn(java.util.Optional.of(wallet));

        assertThrows(InvalidAmountException.class, () -> walletService.withdrawFunds(1L, BigDecimal.valueOf(-52L)));
    }


    @Test
    public void testGetTransactions() {
        Wallet wallet = new Wallet(1L, "Test Customer", BigDecimal.ZERO);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, wallet, new BigDecimal("100"), TransactionType.CREDIT, LocalDateTime.now()));
        transactions.add(new Transaction(1L, wallet, new BigDecimal("50"), TransactionType.CREDIT, LocalDateTime.now()));

        when(walletRepository.findById(any())).thenReturn(java.util.Optional.of(wallet));
        when(transactionRepository.findByWallet(any(), any())).thenReturn(new PageImpl(transactions));
        Page<Transaction> result = walletService.getTransactions(1L, 0, 10);

        assertEquals(transactions, result.getContent());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getNumberOfElements());
    }
}
