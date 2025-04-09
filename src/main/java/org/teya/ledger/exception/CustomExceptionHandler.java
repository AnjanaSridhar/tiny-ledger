package org.teya.ledger.exception;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.teya.ledger.model.ErrorResponse;

@ControllerAdvice
@ResponseBody
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AccountNotFoundException.class})
    public ErrorResponse handleAccountNotFoundException(AccountNotFoundException ex) {
        return new ErrorResponse("BAD_REQUEST", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ErrorResponse("INVALID_REQUEST", "Request params are invalid: " + ex.getParameter().getParameterName());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ErrorResponse("BAD_REQUEST", Objects.requireNonNull(ex.getFieldError()).getField() + ": " + ex.getFieldError().getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ErrorResponse("INVALID_REQUEST", "Request params are invalid: " + ex.getMessage());
    }
}
