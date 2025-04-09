package org.teya.ledger.repository;

import java.util.List;
import java.util.UUID;
import org.teya.ledger.model.Account;

public class LedgerRepository {
    public List<Account> getAccounts() {
        return List.of(
                new Account(UUID.fromString("7da15e01-90be-4026-bd24-027dc3a43792"), "Jane Doe", "ACTIVE"),
                new Account(UUID.fromString("0da69877-6bcb-4b46-b98e-d74c09fd45ce"), "John Doe", "ACTIVE"),
                new Account(UUID.fromString("f1662544-e27a-4227-933a-f9084137160e"), "Buzz Doe", "ACTIVE")
        );
    }
}
