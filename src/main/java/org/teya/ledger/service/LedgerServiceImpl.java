package org.teya.ledger.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.teya.ledger.model.Transaction;
import org.teya.ledger.repository.LedgerRepository;

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
}
