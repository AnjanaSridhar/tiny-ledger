package org.teya.ledger.model;

import java.math.BigDecimal;

public record UpdateLedgerRequest(BigDecimal amount, String description) {
}
