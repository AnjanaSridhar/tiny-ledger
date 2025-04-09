package org.teya.ledger.model;

import java.math.BigDecimal;
import java.util.UUID;

public final class BalanceBuilder {
    private UUID id = UUID.randomUUID();
    private UUID accountId;
    private String type = "CLOSINGBOOKED";
    private BigDecimal balance = new BigDecimal("600.00");

    private BalanceBuilder() {
    }

    public static BalanceBuilder aBalance() {
        return new BalanceBuilder();
    }

    public BalanceBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public BalanceBuilder withAccountId(UUID accountId) {
        this.accountId = accountId;
        return this;
    }

    public BalanceBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public Balance build() {
        return new Balance(id, accountId, type, balance);
    }
}
