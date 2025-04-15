package org.teya.ledger.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import org.teya.ledger.model.Account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.teya.ledger.model.Type.CREDIT;
import static org.teya.ledger.model.Type.DEBIT;

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
        BigDecimal amount1 = new BigDecimal(200);
        ledgerRepository.setInPlayOperations(accountID, CREDIT, amount1, "description");
        BigDecimal amount2 = new BigDecimal(100);
        ledgerRepository.setInPlayOperations(accountID, DEBIT, amount2, "description");
        BigDecimal currentBalance = ledgerRepository.getBalances(accountID).balance().value();

        ledgerRepository.commitOperation(accountID);

        BigDecimal newBalance = ledgerRepository.getBalances(accountID).balance().value();
        assertNotEquals(currentBalance, newBalance);
        assertEquals(currentBalance.add(amount1).subtract(amount2), newBalance);
    }
}
