package org.teya.ledger.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teya.ledger.model.AccountsResponse;
import org.teya.ledger.model.BalancesResponse;
import org.teya.ledger.model.DepositRequest;
import org.teya.ledger.service.LedgerService;
import org.teya.ledger.model.TransactionsResponse;

@RestController
@RequestMapping("/v1/ledger/accounts")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("")
    public ResponseEntity<AccountsResponse> accounts() {
        return ResponseEntity.ok(new AccountsResponse(ledgerService.getAccounts()));
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<BalancesResponse> deposit(@PathVariable("accountId") UUID accountId,
                                                    @RequestBody DepositRequest request) {
        return ResponseEntity.ok(new BalancesResponse(ledgerService.deposit(accountId, request)));
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable("accountId") UUID accountId) {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{accountId}/balances")
    public ResponseEntity<String> balances(@PathVariable("accountId") UUID accountId) {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionsResponse> transactions(@PathVariable("accountId") UUID accountId) {
        return ResponseEntity.ok(new TransactionsResponse(ledgerService.getTransactions(accountId)));
    }
}
