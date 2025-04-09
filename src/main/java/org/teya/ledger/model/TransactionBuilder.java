package org.teya.ledger.model;

import java.math.BigDecimal;
import java.util.UUID;

import static java.time.ZonedDateTime.now;

public final class TransactionBuilder {
    private UUID id = UUID.randomUUID();
    private UUID accountId;
    private String description = "REF " + now().getMonth().name() + now().getDayOfMonth() + now().withFixedOffsetZone() + " Transfer";
    private Amount amount =  new Amount("GBP", new BigDecimal("100.00"));
    private String bookingDateTime = now().withFixedOffsetZone().toString();
    private Type type = Type.CREDIT;

    private TransactionBuilder() {
    }

    public static TransactionBuilder aTransaction() {
        return new TransactionBuilder();
    }

    public TransactionBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TransactionBuilder withType(Type type) {
        this.type = type;
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

    public TransactionBuilder withAmount(Amount amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder withBookingDateTime(String bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
        return this;
    }

    public Transaction build() {
        return new Transaction(id, accountId, description, bookingDateTime, amount, type);
    }
}
