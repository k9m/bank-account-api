package org.k9m.assignments.bankaccountapi.api;

import org.k9m.assignments.bankaccountapi.config.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
