package org.teya.ledger.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.teya.ledger.model.AccountsResponse;
import org.teya.ledger.model.TransactionsResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LedgerControllerTest {
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setup() {
        testRestTemplate = new TestRestTemplate();
    }

    @Test
    void getTransactions_should_return_transactions_successfully() {
        ResponseEntity<TransactionsResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/7da15e01-90be-4026-bd24-027dc3a43792/transactions", HttpMethod.GET, null, TransactionsResponse.class);
        TransactionsResponse transactionsResponse = response.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(transactionsResponse);
        assertEquals(3, transactionsResponse.transactions().size());
    }

    @Test
    void getAccounts_should_return_accounts_successfully() {
        ResponseEntity<AccountsResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts", HttpMethod.GET, null, AccountsResponse.class);
        AccountsResponse accountsResponse = response.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(accountsResponse);
        assertEquals(3, accountsResponse.accounts().size());
        assertNotNull(accountsResponse.accounts().get(0).id());
        assertNotNull(accountsResponse.accounts().get(0).accountHolderName());
        assertNotNull(accountsResponse.accounts().get(0).status());
    }

}
