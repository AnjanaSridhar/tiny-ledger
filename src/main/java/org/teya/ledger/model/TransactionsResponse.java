package org.teya.ledger.model;

import java.util.List;

public record TransactionsResponse(List<Transaction> transactions) {
}
