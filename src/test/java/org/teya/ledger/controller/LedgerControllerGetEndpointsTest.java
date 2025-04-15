package org.teya.ledger.controller;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.teya.ledger.model.AccountsResponse;
import org.teya.ledger.model.BalancesResponse;
import org.teya.ledger.model.ErrorResponse;
import org.teya.ledger.model.TransactionsResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableAutoConfiguration
public class LedgerControllerGetEndpointsTest {
    private TestRestTemplate testRestTemplate;

    private static final String ACCOUNT_ID = "0da69877-6bcb-4b46-b98e-d74c09fd45ce";
    private static final String INVALID_ACCOUNT_ID = "invalid-account-id";

    @BeforeEach
    public void setup() {
        testRestTemplate = new TestRestTemplate();
    }

    /*
     * Success scenarios for all GET endpoints
     * */
    @Test
    void getAccounts_should_return_accounts_successfully() {
        ResponseEntity<AccountsResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts", HttpMethod.GET, null, AccountsResponse.class);
        AccountsResponse accountsResponse = response.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(3, accountsResponse.accounts().size());
        assertNotNull(accountsResponse.accounts().get(0).id());
        assertNotNull(accountsResponse.accounts().get(0).accountHolderName());
        assertNotNull(accountsResponse.accounts().get(0).status());
    }

    @Test
    void getTransactions_should_return_transactions_successfully() {
        ResponseEntity<TransactionsResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/transactions", HttpMethod.GET, null, TransactionsResponse.class);
        TransactionsResponse transactionsResponse = response.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertNotEquals(0, transactionsResponse.transactions().size());
        assertNotNull(transactionsResponse.transactions().get(0).id());
        assertNotNull(transactionsResponse.transactions().get(0).accountId());
        assertNotNull(transactionsResponse.transactions().get(0).bookingDateTime());
        assertNotNull(transactionsResponse.transactions().get(0).description());
        assertNotNull(transactionsResponse.transactions().get(0).type());
        assertNotNull(transactionsResponse.transactions().get(0).amount());
        assertNotNull(transactionsResponse.transactions().get(0).amount().value());
        assertNotNull(transactionsResponse.transactions().get(0).amount().currency());
    }

    @Test
    void getBalance_should_return_balance_successfully() {
        ResponseEntity<BalancesResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/balances", HttpMethod.GET, null, BalancesResponse.class);
        BalancesResponse balancesResponse = response.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(balancesResponse.balance());
        assertNotNull(balancesResponse.balance().balance());
        assertNotNull(balancesResponse.balance().balance().value());
        assertNotNull(balancesResponse.balance().balance().currency());
        assertNotNull(balancesResponse.balance().accountId());
        assertNotNull(balancesResponse.balance().type());
    }

    /*
    * Validation of GET requests
    * */
    @Test
    void getTransactions_should_return_400_whenAccountIdIsInvalid() {
        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + INVALID_ACCOUNT_ID + "/transactions", HttpMethod.GET, null, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("INVALID_REQUEST", errorResponse.error());
        assertEquals("Request params are invalid: accountId", errorResponse.errorMessage());
    }

    @Test
    void getTransactions_should_return_400_whenAccountIsNotFound() {
        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/transactions", HttpMethod.GET, null, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertEquals("Account with id b2a34139-f893-461e-91ce-f01ff691ab69 not found.", errorResponse.errorMessage());
    }

    @Test
    void getBalances_should_return_400_whenAccountIdIsInvalid() {
        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + INVALID_ACCOUNT_ID + "/balances", HttpMethod.GET, null, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("INVALID_REQUEST", errorResponse.error());
        assertEquals("Request params are invalid: accountId", errorResponse.errorMessage());
    }

    @Test
    void getBalances_should_return_400_whenAccountIsNotFound() {
        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/balances", HttpMethod.GET, null, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertEquals("Account with id b2a34139-f893-461e-91ce-f01ff691ab69 not found.", errorResponse.errorMessage());
    }
}
