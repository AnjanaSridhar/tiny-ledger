package org.teya.ledger.model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record UpdateLedgerRequest(
        @NotNull(message = "amount: cannot be null or empty")
        @DecimalMin(value = "0.00", inclusive = false)
        BigDecimal amount,
        @Nullable
        String description) {
}
