package org.teya.ledger.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.Balance;
import org.teya.ledger.model.OperationType;
import org.teya.ledger.model.Transaction;
import org.teya.ledger.model.UpdateLedgerRequest;
import org.teya.ledger.repository.LedgerRepository;

import static org.teya.ledger.model.OperationType.DEPOSIT;
import static org.teya.ledger.model.OperationType.WITHDRAW;

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
    public Balance updateLedger(UUID accountId, UpdateLedgerRequest request, OperationType operation) {
        BigDecimal balance = ledgerRepository.getBalances(accountId).balance().value();
        if (operation == WITHDRAW) {
            ledgerRepository.updateBalance(accountId, computeBalanceAfterWithdrawal(balance, request.amount()));
            ledgerRepository.updateTransaction(accountId);
        } else if (operation == DEPOSIT) {
            ledgerRepository.updateBalance(accountId, computeBalanceAfterDeposit(balance, request.amount()));
            ledgerRepository.updateTransaction(accountId);
        }
        return ledgerRepository.getBalances(accountId);
    }

    @Override
    public Balance getBalances(UUID accountId) {
        return ledgerRepository.getBalances(accountId);
    }

    private static BigDecimal computeBalanceAfterDeposit(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.add(amount);
    }

    private static BigDecimal computeBalanceAfterWithdrawal(BigDecimal currentBalance, BigDecimal amount) {
        return currentBalance.subtract(amount);
    }
}
