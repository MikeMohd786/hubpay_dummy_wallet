package com.hubpay.dummy_wallet.controllers;

import com.hubpay.dummy_wallet.controllers.requests.WalletTransactionRequest;
import com.hubpay.dummy_wallet.persistance.entities.Transaction;
import com.hubpay.dummy_wallet.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    private WalletController walletController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        walletController = new WalletController(walletService);
    }

    @Test
    public void testAddFundsToWallet() {
        Long walletId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        ResponseEntity<String> response = walletController.addFundsToWallet(walletId, new WalletTransactionRequest(amount));

        verify(walletService, times(1)).addFunds(walletId, amount);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void testWithdrawFundsFromWallet() {
        Long walletId = 1L;
        BigDecimal amount = new BigDecimal("50.00");
        ResponseEntity<String> response = walletController.withdrawFundsFromWallet(walletId, new WalletTransactionRequest(amount));

        verify(walletService, times(1)).withdrawFunds(walletId, amount);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void testGetTransactionsWithResults() {
        Long walletId = 1L;
        Integer page = 0;
        Integer size = 10;

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());
        Page<Transaction> pageResult = new PageImpl<>(transactions);

        when(walletService.getTransactions(walletId, page, size)).thenReturn(pageResult);

        ResponseEntity<Page<Transaction>> response = walletController.getTransactions(walletId, page, size);

        verify(walletService, times(1)).getTransactions(walletId, page, size);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResult, response.getBody());
    }

    @Test
    public void testGetTransactionsWithNoResults() {
        Long walletId = 1L;
        Integer page = 0;
        Integer size = 10;

        Page<Transaction> pageResult = new PageImpl<>(new ArrayList<>());

        when(walletService.getTransactions(walletId, page, size)).thenReturn(pageResult);

        ResponseEntity<Page<Transaction>> response = walletController.getTransactions(walletId, page, size);

        verify(walletService, times(1)).getTransactions(walletId, page, size);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}

