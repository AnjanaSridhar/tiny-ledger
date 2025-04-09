package org.teya.ledger.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teya.ledger.exception.AccountNotFoundException;
import org.teya.ledger.model.AccountsResponse;
import org.teya.ledger.model.BalancesResponse;
import org.teya.ledger.model.UpdateLedgerRequest;
import org.teya.ledger.service.LedgerService;
import org.teya.ledger.model.TransactionsResponse;

import static org.teya.ledger.model.OperationType.DEPOSIT;
import static org.teya.ledger.model.OperationType.WITHDRAW;

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
                                                    @Valid @RequestBody UpdateLedgerRequest body) {
        if(isAccountExists(accountId)) {
            return ResponseEntity.ok(new BalancesResponse(ledgerService.updateLedger(accountId, body, DEPOSIT)));
        }
        throw new AccountNotFoundException(accountId);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<BalancesResponse> withdraw(@PathVariable("accountId") UUID accountId,
                                                     @Valid @RequestBody UpdateLedgerRequest body) {
        if(isAccountExists(accountId)) {
            return ResponseEntity.ok(new BalancesResponse(ledgerService.updateLedger(accountId, body, WITHDRAW)));
        }
        throw new AccountNotFoundException(accountId);
    }

    @GetMapping("/{accountId}/balances")
    public ResponseEntity<BalancesResponse> balances(@PathVariable("accountId") UUID accountId) {
        if(isAccountExists(accountId)) {
            return ResponseEntity.ok(new BalancesResponse(ledgerService.getBalances(accountId)));
        }
        throw new AccountNotFoundException(accountId);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionsResponse> transactions(@PathVariable("accountId") UUID accountId) {
        if(isAccountExists(accountId)) {
            return ResponseEntity.ok(new TransactionsResponse(ledgerService.getTransactions(accountId)));
        }
        throw new AccountNotFoundException(accountId);
    }

    private boolean isAccountExists(UUID accountId) {
        return ledgerService.getAccounts().stream()
                .anyMatch(account -> accountId.equals(account.id()));
    }
}
