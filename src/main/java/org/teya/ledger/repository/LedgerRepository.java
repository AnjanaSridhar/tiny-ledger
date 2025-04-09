package org.teya.ledger.repository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.Transaction;

import static org.teya.ledger.model.TransactionBuilder.aTransaction;

public class LedgerRepository {
    private final HashMap<UUID, List<Transaction>> transactionsMap;

    public LedgerRepository() {
        this.transactionsMap = new HashMap<>();
        loadMockData();
    }

    public List<Account> getAccounts() {
        return List.of(
                new Account(UUID.fromString("7da15e01-90be-4026-bd24-027dc3a43792"), "Jane Doe", "ACTIVE"),
                new Account(UUID.fromString("0da69877-6bcb-4b46-b98e-d74c09fd45ce"), "John Doe", "ACTIVE"),
                new Account(UUID.fromString("f1662544-e27a-4227-933a-f9084137160e"), "Buzz Doe", "ACTIVE")
        );
    }

    public List<Transaction> getTransactions(UUID accountId) {
        return transactionsMap.get(accountId);
    }

    private void loadMockData() {
        getAccounts().stream()
                .peek(account -> transactionsMap.put(account.id(), List.of(aTransaction().withAccountId(account.id()).build(),
                        aTransaction().withAccountId(account.id()).build(),
                        aTransaction().withAccountId(account.id()).build()))).toList();
    }
}
