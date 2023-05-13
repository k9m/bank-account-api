package org.k9m.assignments.bankaccountapi.config.exception;

import lombok.Getter;
import org.k9m.assignments.bankaccountapi.api.model.ErrorObjectDTO;
import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    @Getter
    private final HttpStatus statusCode;

    public ApplicationException(final HttpStatus statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ErrorObjectDTO toError() {
        return new ErrorObjectDTO()
                .statusCode(statusCode.value())
                .message(getMessage());
    }

}
