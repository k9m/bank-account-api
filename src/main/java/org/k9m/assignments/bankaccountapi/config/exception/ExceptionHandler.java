package org.k9m.assignments.bankaccountapi.config.exception;

import lombok.val;
import org.k9m.assignments.bankaccountapi.api.model.ErrorObjectDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        val applicationException = (ApplicationException) ex;

        return new ResponseEntity<>(applicationException.toError(), new HttpHeaders(), applicationException.getStatusCode());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(
                new ErrorObjectDTO()
                        .statusCode(status.value())
                        .message(ex.getMessage()),
                headers,
                status);
    }
}