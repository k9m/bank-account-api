package org.k9m.assignments.bankaccountapi.api;

import org.k9m.assignments.bankaccountapi.config.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends ApplicationException {
    public InsufficientFundsException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE, message);
    }
}
