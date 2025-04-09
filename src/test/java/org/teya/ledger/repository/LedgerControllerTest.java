package org.teya.ledger.repository;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.teya.ledger.model.AccountsResponse;
import org.teya.ledger.model.BalancesResponse;
import org.teya.ledger.model.UpdateLedgerRequest;
import org.teya.ledger.model.Transaction;
import org.teya.ledger.model.TransactionsResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LedgerControllerTest {
    private TestRestTemplate testRestTemplate;

    private static final String ACCOUNT_ID = "0da69877-6bcb-4b46-b98e-d74c09fd45ce";

    @BeforeEach
    public void setup() {
        testRestTemplate = new TestRestTemplate();
    }

    @Test
    void getTransactions_should_return_transactions_successfully() {
        ResponseEntity<TransactionsResponse> response = testRestTemplate.exchange("http://localhost:8080/v1/ledger/accounts/" + ACCOUNT_ID + "/transactions", HttpMethod.GET, null, TransactionsResponse.class);
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
        assertTrue(result.get().description().contains("Withdrawal"));

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
        assertEquals("description", result.get().description());
    }

}
