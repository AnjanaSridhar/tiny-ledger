package org.teya.ledger.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import org.teya.ledger.model.Account;
import org.teya.ledger.model.Amount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.teya.ledger.model.TransactionBuilder.aTransaction;

@Repository
public class LedgerRepositoryTest {
    private LedgerRepository ledgerRepository;

    @BeforeEach
    public void setUp() {
        ledgerRepository = new LedgerRepository();
    }

    @Test
    void should_load_mockAccounts_successfully() {
        assertNotEquals(0, ledgerRepository.getAccounts().size());
    }

    @Test
    void should_load_mockTransactions_successfully() {
        List<Account> accounts = ledgerRepository.getAccounts();
        assertNotEquals(0, ledgerRepository.getTransactions(accounts.getFirst().id()).size());
    }

    @Test
    void should_load_mockBalances_successfully() {
        List<Account> accounts = ledgerRepository.getAccounts();
        assertNotNull(ledgerRepository.getBalances(accounts.getFirst().id()));
    }

    @Test
    void should_update_Transactions_InPlay_WhenCommitted() {
        List<Account> accounts = ledgerRepository.getAccounts();
        UUID accountID = accounts.get(0).id();
        BigDecimal amount1 = new BigDecimal(100);
        ledgerRepository.setInPlayOperations(accountID, aTransaction().withAmount(new Amount("GBP", amount1)).build());
        BigDecimal amount2 = new BigDecimal(100);
        ledgerRepository.setInPlayOperations(accountID, aTransaction().withAmount(new Amount("GBP", amount2)).build());
        BigDecimal currentBalance = ledgerRepository.getBalances(accountID).balance().value();

        ledgerRepository.commitOperation(accountID);

        BigDecimal newBalance = ledgerRepository.getBalances(accountID).balance().value();
        assertNotEquals(currentBalance, newBalance);
        assertEquals(currentBalance.add(amount1).add(amount2), newBalance);
    }
}
