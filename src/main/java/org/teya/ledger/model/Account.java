package org.teya.ledger.model;

import java.util.UUID;

public record Account(UUID id, String accountHolderName, String status) {
}
