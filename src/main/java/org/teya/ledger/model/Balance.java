package org.teya.ledger.model;

import java.util.UUID;

public record Balance(UUID id, UUID accountId, String type, Amount balance) {
}
