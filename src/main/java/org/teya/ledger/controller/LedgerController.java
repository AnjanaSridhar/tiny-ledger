package org.teya.ledger.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ledger/accounts")
public class LedgerController {
    @GetMapping("")
    public ResponseEntity<String> accounts() {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable("accountId") UUID accountId) {
        return ResponseEntity.status(501).build();
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
    public ResponseEntity<String> transactions(@PathVariable("accountId") UUID accountId) {
        return ResponseEntity.status(501).build();
    }
}
