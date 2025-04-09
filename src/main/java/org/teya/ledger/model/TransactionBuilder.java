package org.teya.ledger.model;

import java.math.BigDecimal;
import java.util.UUID;

import static java.time.ZonedDateTime.now;

public final class TransactionBuilder {
    private UUID id = UUID.randomUUID();
    private UUID accountId;
    private String description = "description";
    private String bookingDateTime = now().withFixedOffsetZone().toString();
    private BigDecimal amount = BigDecimal.TEN;

    private TransactionBuilder() {
    }

    public static TransactionBuilder aTransaction() {
        return new TransactionBuilder();
    }

    public TransactionBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TransactionBuilder withAccountId(UUID accountId) {
        this.accountId = accountId;
        return this;
    }

    public TransactionBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransactionBuilder withBookingDateTime(String bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
        return this;
    }

    public TransactionBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Transaction build() {
        return new Transaction(id, accountId, description, bookingDateTime, amount);
    }
}
