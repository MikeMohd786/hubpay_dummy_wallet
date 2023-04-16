package com.hubpay.dummy_wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class WalletNotFoundException extends HttpClientErrorException {
    public WalletNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}