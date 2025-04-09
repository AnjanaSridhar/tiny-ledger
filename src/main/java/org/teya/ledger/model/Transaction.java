package org.teya.ledger.model;

import java.util.UUID;

public record Transaction(UUID id, UUID accountId, String description, String bookingDateTime) {
}
