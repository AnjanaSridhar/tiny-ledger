import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.teya.ledger.repository.LedgerRepository;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
}
