package com.hubpay.dummy_wallet.controllers;

import com.hubpay.dummy_wallet.controllers.requests.WalletTransactionRequest;
import com.hubpay.dummy_wallet.persistance.entities.Transaction;
import com.hubpay.dummy_wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet/{walletId}")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/add-funds")
    public ResponseEntity<String> addFundsToWallet(@PathVariable Long walletId, @RequestBody WalletTransactionRequest request) {
        walletService.addFunds(walletId, request.getAmount());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/withdraw-funds")
    public ResponseEntity<String> withdrawFundsFromWallet(@PathVariable Long walletId, @RequestBody WalletTransactionRequest request) {
        walletService.withdrawFunds(walletId, request.getAmount());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/transactions")
    public ResponseEntity<Page<Transaction>> getTransactions(@PathVariable Long walletId,
                                                             @RequestParam(defaultValue = "0") Integer  page,
                                                             @RequestParam(defaultValue = "10") Integer  size) {
        Page<Transaction> transactions = walletService.getTransactions(walletId, page, size);

        if (transactions.getTotalElements() == 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(transactions);
        }
    }


}
