package org.teya.ledger.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.DepositRequest;
import org.teya.ledger.model.Transaction;
import org.teya.ledger.repository.LedgerRepository;

import static org.teya.ledger.model.TransactionBuilder.aTransaction;

@Service
public class LedgerServiceImpl implements LedgerService {
    private final LedgerRepository ledgerRepository;

    public LedgerServiceImpl(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    public List<Transaction> getTransactions(UUID accountId) {
        return ledgerRepository.getTransactions(accountId);
    }

    @Override
    public List<Account> getAccounts() {
        return ledgerRepository.getAccounts();
    }

    @Override
    public BigDecimal deposit(UUID accountId, DepositRequest request) {
        ledgerRepository.updateTransaction(accountId, aTransaction().build());
        return BigDecimal.ZERO;
    }
}
