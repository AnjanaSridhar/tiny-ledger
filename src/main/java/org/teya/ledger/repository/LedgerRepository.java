package org.teya.ledger.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.Amount;
import org.teya.ledger.model.Balance;
import org.teya.ledger.model.Transaction;
import org.teya.ledger.model.Type;

import static org.teya.ledger.model.BalanceBuilder.aBalance;
import static org.teya.ledger.model.TransactionBuilder.aTransaction;
import static org.teya.ledger.model.Type.CREDIT;

@Repository
public class LedgerRepository {
    private final HashMap<UUID, List<Transaction>> transactionsMap;
    private final HashMap<UUID, Balance> balancesMap;
    private final HashMap<UUID, List<Transaction>> operationsMap;

    public LedgerRepository() {
        this.transactionsMap = new HashMap<>();
        this.balancesMap = new HashMap<>();
        this.operationsMap = new HashMap<>();
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
                .peek(account -> {
                    transactionsMap.put(account.id(), List.of(aTransaction().withAccountId(account.id()).build(),
                            aTransaction().withAccountId(account.id()).withAmount(new Amount("GBP", new BigDecimal("200.00"))).build(),
                            aTransaction().withAccountId(account.id()).withAmount(new Amount("GBP", new BigDecimal("300.00"))).build()));
                    balancesMap.put(account.id(), aBalance().withAccountId(account.id()).build());
                }).toList();

    }

    public void updateTransaction(UUID accountId, Type type, BigDecimal amount, String description) {
        List<Transaction> transactions = new ArrayList<>(transactionsMap.get(accountId));
        transactions.add(aTransaction()
                .withAccountId(accountId)
                .withType(type)
                .withAmount(new Amount("GBP", amount))
                .withDescription(description).build());
        transactionsMap.put(accountId, transactions);
    }

    public void updateTransaction(UUID accountId, List<Transaction> transactions) {
        transactionsMap.put(accountId, transactions);
    }

    public void updateBalance(UUID accountId, BigDecimal balance) {
        balancesMap.put(accountId, aBalance()
                .withAccountId(accountId)
                .withBalance(new Amount("GBP", balance)).build());
    }

    public Balance getBalances(UUID accountId) {
        return balancesMap.get(accountId);
    }

    public void setInPlayOperations(UUID accountId, Transaction transaction) {
        if(operationsMap.get(accountId) != null && !operationsMap.get(accountId).isEmpty()) {
            List<Transaction> inPlayTransactions = new ArrayList<>(operationsMap.get(accountId));
            inPlayTransactions.add(transaction);
            operationsMap.put(accountId, inPlayTransactions);
        }
        else{
            operationsMap.put(accountId, List.of(transaction));
        }
    }

    public void commitOperation(UUID accountId) {
        if(!operationsMap.get(accountId).isEmpty()) {
            List<Transaction> transactions = operationsMap.get(accountId);
            List<Transaction> inPlayTransactions = new ArrayList<>(transactionsMap.get(accountId));
            inPlayTransactions.addAll(transactions);
            updateTransaction(accountId, inPlayTransactions);
            transactions.forEach(transaction ->
                    updateBalance(accountId, computeBalance(getBalances(accountId).balance().value(), transaction)));
        }
    }

    private BigDecimal computeBalance(BigDecimal currentBalance, Transaction transaction) {
        if(CREDIT == transaction.type()) return currentBalance.add(transaction.amount().value());
        else return currentBalance.subtract(transaction.amount().value());
    }
}
