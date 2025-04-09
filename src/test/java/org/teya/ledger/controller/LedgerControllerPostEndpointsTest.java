package org.teya.ledger.controller;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.teya.ledger.model.BalancesResponse;
import org.teya.ledger.model.ErrorResponse;
import org.teya.ledger.model.Transaction;
import org.teya.ledger.model.TransactionsResponse;
import org.teya.ledger.model.UpdateLedgerRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableAutoConfiguration
public class LedgerControllerPostEndpointsTest {
    private TestRestTemplate testRestTemplate;

    private static final String ACCOUNT_ID = "0da69877-6bcb-4b46-b98e-d74c09fd45ce";
    private static final String INVALID_ACCOUNT_ID = "invalid-account-id";

    @BeforeEach
    public void setup() {
        testRestTemplate = new TestRestTemplate();
    }

    /*
    * Success scenarios for POST requests
    * */
    @Test
    void postWithdraw_should_update_transactionsAndBalances_successfully() {
        BigDecimal input = BigDecimal.TEN;
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(input, null));

        ResponseEntity<BalancesResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/withdraw", HttpMethod.POST, requestBody, BalancesResponse.class);
        BalancesResponse balancesResponse = response.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(balancesResponse.balance());
        //check if balance was updated after withdrawal of 10 GBP
        assertEquals(new BigDecimal("590.00"), balancesResponse.balance().balance().value());
        //check if transaction was added
        ResponseEntity<TransactionsResponse> transactionResponse = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/transactions", HttpMethod.GET, null, TransactionsResponse.class);
        TransactionsResponse transactionsResponse = transactionResponse.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(4, transactionsResponse.transactions().size());
        Optional<Transaction> result = transactionsResponse.transactions().stream()
                .filter(transaction -> transaction.amount().value().equals(input)).findAny();
        assertTrue(result.isPresent());
        assertTrue(result.get().description().contains("Withdraw"));

    }

    @Test
    void postDeposit_should_update_transactionsAndBalances_successfully() {
        //Given current balance
        ResponseEntity<BalancesResponse> currentBalanceResponse = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/balances", HttpMethod.GET, null, BalancesResponse.class);
        BigDecimal currentBalance = currentBalanceResponse.getBody().balance().balance().value();
        //When a deposit of 20 GBP is made
        BigDecimal input = new BigDecimal(20);
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(input, "description"));
        ResponseEntity<BalancesResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/deposit", HttpMethod.POST, requestBody, BalancesResponse.class);
        BalancesResponse balancesResponse = response.getBody();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(balancesResponse.balance());
        //check if balance was updated after deposit of 10 GBP
        assertEquals(currentBalance.add(input), balancesResponse.balance().balance().value());
        //check if transaction was added
        ResponseEntity<TransactionsResponse> transactionResponse = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/transactions", HttpMethod.GET, null, TransactionsResponse.class);
        TransactionsResponse transactionsResponse = transactionResponse.getBody();
        assertEquals(200, response.getStatusCode().value());
        Optional<Transaction> result = transactionsResponse.transactions().stream()
                .filter(transaction -> transaction.amount().value().equals(input)).findAny();
        assertTrue(result.isPresent());
        assertTrue(result.get().description().equals("description"));
    }

    /*
     * Validation of POST requests
     * */
    @Test
    void postWithdraw_should_return_400_whenAccountIdIsInvalid() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(BigDecimal.TEN, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + INVALID_ACCOUNT_ID + "/withdraw", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("INVALID_REQUEST", errorResponse.error());
        assertEquals("Request params are invalid: accountId", errorResponse.errorMessage());
    }

    @Test
    void postWithdraw_should_return_400_whenAccountIsNotFound() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(BigDecimal.TEN, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/withdraw", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertEquals("Account with id b2a34139-f893-461e-91ce-f01ff691ab69 not found.", errorResponse.errorMessage());
    }

    @Test
    void postDeposit_should_return_400_whenAccountIdIsInvalid() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(BigDecimal.TEN, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + INVALID_ACCOUNT_ID + "/deposit", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("INVALID_REQUEST", errorResponse.error());
        assertEquals("Request params are invalid: accountId", errorResponse.errorMessage());
    }

    @Test
    void postDeposit_should_return_400_whenAccountIsNotFound() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(BigDecimal.TEN, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/deposit", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertEquals("Account with id b2a34139-f893-461e-91ce-f01ff691ab69 not found.", errorResponse.errorMessage());
    }

    @Test
    void postWithdraw_should_return_400_whenAmountIsNotPassed() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(null, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/withdraw", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertTrue(errorResponse.errorMessage().contains("amount: cannot be null or empty"));
    }

    @Test
    void postDeposit_should_return_400_whenAmountIsNotPassed() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(null, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/deposit", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertTrue(errorResponse.errorMessage().contains("amount: cannot be null or empty"));
    }

    @Test
    void postWithdraw_should_return_400_whenAmountPassedIsZero() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(BigDecimal.ZERO, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/withdraw", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertEquals("amount: must be greater than 0.00", errorResponse.errorMessage());
    }

    @Test
    void postDeposit_should_return_400_whenAmountPassedIsZero() {
        HttpEntity<UpdateLedgerRequest> requestBody = new HttpEntity<>(new UpdateLedgerRequest(BigDecimal.ZERO, "description"));

        ResponseEntity<ErrorResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + UUID.fromString("b2a34139-f893-461e-91ce-f01ff691ab69") + "/deposit", HttpMethod.POST, requestBody, ErrorResponse.class);
        ErrorResponse errorResponse = response.getBody();
        assertEquals(400, response.getStatusCode().value());
        assertEquals("BAD_REQUEST", errorResponse.error());
        assertEquals("amount: must be greater than 0.00", errorResponse.errorMessage());
    }


}
