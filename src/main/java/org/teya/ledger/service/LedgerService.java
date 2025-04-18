package org.teya.ledger.service;

import java.util.List;
import java.util.UUID;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.Balance;
import org.teya.ledger.model.OperationType;
import org.teya.ledger.model.UpdateLedgerRequest;
import org.teya.ledger.model.Transaction;

public interface LedgerService {
    List<Transaction> getTransactions(UUID accountId);

    List<Account> getAccounts();

    Balance updateLedger(UUID accountId, UpdateLedgerRequest request, OperationType withdraw);

    Balance getBalances(UUID accountId);

    void beginTransaction(UUID operationId, Transaction transaction);

    void commitTransaction(String status, UUID operationId);
}
