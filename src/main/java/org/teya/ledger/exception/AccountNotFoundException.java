package org.teya.ledger.exception;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(UUID id) {
        super("Account with id " + id + " not found.");
    }
}
