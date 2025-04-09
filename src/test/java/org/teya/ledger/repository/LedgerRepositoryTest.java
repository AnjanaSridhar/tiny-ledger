package org.teya.ledger.repository;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import org.teya.ledger.model.Account;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
