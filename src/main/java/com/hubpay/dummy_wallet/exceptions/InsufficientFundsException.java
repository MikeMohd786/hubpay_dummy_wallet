package com.hubpay.dummy_wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class InsufficientFundsException extends HttpClientErrorException {
    public InsufficientFundsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
