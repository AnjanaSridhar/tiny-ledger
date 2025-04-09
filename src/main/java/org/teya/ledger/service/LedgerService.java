package org.teya.ledger.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.DepositRequest;
import org.teya.ledger.model.Transaction;

public interface LedgerService {
    List<Transaction> getTransactions(UUID accountId);

    List<Account> getAccounts();

    BigDecimal deposit(UUID accountId, DepositRequest request);
}
