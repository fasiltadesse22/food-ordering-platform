package com.foodordering.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * C2.1.2-P02 learning Value Object for monetary semantics.
 *
 * Deliberately narrow: scale=2 and HALF_EVEN are explicit learning-policy
 * choices, not a claim that every currency/business uses two decimal places.
 * P02 makes precision, rounding and currency part of the domain type instead
 * of leaving them implicit in BigDecimal call sites.
 */
public record Money(BigDecimal amount, Currency currency) {
    public static final int SCALE = 2;
    public static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;

    public Money {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(currency, "currency");
        amount = amount.setScale(SCALE, ROUNDING);
        if (amount.signum() < 0) throw new IllegalArgumentException("money amount must not be negative");
    }

    public static Money of(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity, "quantity");
        return new Money(amount.multiply(BigDecimal.valueOf(quantity.value())), currency);
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other");
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("cannot combine different currencies: " + currency + " and " + other.currency);
        }
    }
}
