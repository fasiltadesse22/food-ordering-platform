package com.foodordering.domain;

import java.math.BigDecimal;
import java.util.Currency;

public final class ValueSemanticsTest {
    public static void main(String[] args) {
        equalValuesAreInterchangeable();
        typedValuesRejectInvalidStateAtConstruction();
        replacementDoesNotMutateOriginalValue();
        compositeValueEqualityIncludesComponents();
        currencyIsPartOfMoneyMeaning();
        precisionAndRoundingPolicyAreExplicit();
        typedIdentityUsesValueSemanticsWithoutBecomingEntity();
        primitiveObsessionLosesDomainMeaning();
        System.out.println("PASS ValueSemanticsTest (8 tests)");
    }

    private static void equalValuesAreInterchangeable() {
        require(Money.of("100.00", "ETB").equals(Money.of("100", "ETB")),
                "equal monetary values should compare equal");
        require(new Quantity(2).equals(new Quantity(2)),
                "quantity equality should be structural");
    }

    private static void typedValuesRejectInvalidStateAtConstruction() {
        expectIllegal(() -> new Quantity(0), "quantity");
        expectIllegal(() -> Money.of("-1.00", "ETB"), "negative money");
    }

    private static void replacementDoesNotMutateOriginalValue() {
        var original = new OrderLine("Doro Wot", new Quantity(1), Money.of("450.00", "ETB"));
        var replacement = original.withQuantity(new Quantity(2));

        require(original.quantity().equals(new Quantity(1)), "original value must remain unchanged");
        require(replacement.quantity().equals(new Quantity(2)), "replacement should contain new value");
    }

    private static void compositeValueEqualityIncludesComponents() {
        var first = new OrderLine("Doro Wot", new Quantity(2), Money.of("450", "ETB"));
        var second = new OrderLine("Doro Wot", new Quantity(2), Money.of("450.00", "ETB"));
        require(first.equals(second), "composite values with equal components should be equal");
        require(first.hashCode() == second.hashCode(), "equal values require equal hash codes");
        require(first.subtotal().equals(Money.of("900.00", "ETB")), "behavior should preserve value semantics");
    }

    private static void currencyIsPartOfMoneyMeaning() {
        var etb = Money.of("100", "ETB");
        var usd = Money.of("100", "USD");
        require(!etb.equals(usd), "100 ETB and 100 USD are not the same value");
        expectIllegal(() -> etb.plus(usd), "mixed-currency addition");
    }

    private static void precisionAndRoundingPolicyAreExplicit() {
        var rounded = new Money(new BigDecimal("10.005"), Currency.getInstance("ETB"));
        require(rounded.amount().equals(new BigDecimal("10.00")),
                "HALF_EVEN at scale 2 should make rounding deterministic");
    }

    private static void typedIdentityUsesValueSemanticsWithoutBecomingEntity() {
        var raw = java.util.UUID.randomUUID();
        var first = new OrderId(raw);
        var second = new OrderId(raw);
        require(first.equals(second), "typed identity values compare by represented value");
        require(first != second, "value equality does not require Java object identity");
    }

    private static void primitiveObsessionLosesDomainMeaning() {
        var amount = new BigDecimal("2");
        var quantity = new BigDecimal("2");
        require(amount.equals(quantity),
                "raw primitives can compare equal even when the domain meanings are incompatible");
        require(!Money.of("2", "ETB").equals(new Quantity(2)),
                "semantic types prevent accidental equivalence of different domain concepts");
    }

    private static void expectIllegal(Runnable action, String scenario) {
        try {
            action.run();
            throw new AssertionError("expected IllegalArgumentException for " + scenario);
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
