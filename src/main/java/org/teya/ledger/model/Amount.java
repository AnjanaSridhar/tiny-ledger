package org.teya.ledger.model;

import java.math.BigDecimal;

public record Amount(String currency, BigDecimal value) {
}
