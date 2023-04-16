package com.hubpay.dummy_wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WalletNotFoundException extends ResponseStatusException {
    public WalletNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}