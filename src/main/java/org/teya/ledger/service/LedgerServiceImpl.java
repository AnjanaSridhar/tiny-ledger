package org.teya.ledger.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.Balance;
import org.teya.ledger.model.OperationType;
import org.teya.ledger.model.Transaction;
import org.teya.ledger.model.UpdateLedgerRequest;
import org.teya.ledger.repository.LedgerRepository;

import static java.time.ZonedDateTime.now;
import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.teya.ledger.model.OperationType.DEPOSIT;
import static org.teya.ledger.model.OperationType.WITHDRAW;
import static org.teya.ledger.model.Type.CREDIT;
import static org.teya.ledger.model.Type.DEBIT;

@Service
public class LedgerServiceImpl implements LedgerService {
    private final LedgerRepository ledgerRepository;

    public LedgerServiceImpl(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    public List<Transaction> getTransactions(UUID accountId) {
        List<Transaction> transactions = new ArrayList<>(ledgerRepository.getTransactions(accountId));
        transactions.sort(Comparator.comparing(transaction -> ((Transaction)transaction).bookingDateTime()).reversed());
        return transactions;
    }

    @Override
    public List<Account> getAccounts() {
        return ledgerRepository.getAccounts();
    }

    @Override
    public Balance updateLedger(UUID accountId, UpdateLedgerRequest request, OperationType operation) {
        String description = request.description();
        if (operation == WITHDRAW) {
            if (isEmpty(description)) description = "Withdrawal " + now().getMonth() + now().getDayOfMonth();
            ledgerRepository.setInPlayOperations(accountId, DEBIT, request.amount(), description);
        } else if (operation == DEPOSIT) {
            if (isEmpty(description)) description = "Deposit " + now().getMonth() + now().getDayOfMonth();
            ledgerRepository.setInPlayOperations(accountId, CREDIT, request.amount(), description);
        }
        ledgerRepository.commitOperation(accountId);
        return ledgerRepository.getBalances(accountId);
    }

    @Override
    public Balance getBalances(UUID accountId) {
        return ledgerRepository.getBalances(accountId);
    }

    @Override
    public void beginTransaction(UUID operationId, Transaction transaction) {
        ledgerRepository.setInPlayOperations(operationId, transaction.type(), transaction.amount().value(), transaction.description());
    }

    @Override
    public void commitTransaction(String status, UUID operationId) {
        ledgerRepository.commitOperation(operationId);
    }
}
