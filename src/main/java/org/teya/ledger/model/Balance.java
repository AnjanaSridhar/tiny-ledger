package org.teya.ledger.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Balance(UUID id, UUID accountId, String type, BigDecimal balance) {
}
