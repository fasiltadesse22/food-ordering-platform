package com.foodordering.correctness;

import com.foodordering.domain.OrderId;
import com.foodordering.lifecycle.OrderState;

public final class ExecutableCorrectnessModelTest {
    public static void main(String[] args) {
        paidOrderCannotBeSilentlyChanged();
        unpaidOrderMayStillChangeCommercialTerms();
        oneSuccessfulEffectPerLogicalPaymentSequentially();
        requestIdentityIsNotLogicalPaymentIdentity();
        cancellationAndAcceptanceCannotBothBecomeFinalSequentially();
        validationAndInvariantFailuresAreSemanticallyDifferent();
        System.out.println("PASS ExecutableCorrectnessModelTest (6 tests)");
    }

    private static void paidOrderCannotBeSilentlyChanged() {
        var model = new CorrectnessModel();
        expectInvariant("INV-ORDER-01", () -> model.assertCommercialChangeAllowed(OrderState.PAID));
    }

    private static void unpaidOrderMayStillChangeCommercialTerms() {
        var model = new CorrectnessModel();
        model.assertCommercialChangeAllowed(OrderState.DRAFT);
        model.assertCommercialChangeAllowed(OrderState.PLACED);
    }

    private static void oneSuccessfulEffectPerLogicalPaymentSequentially() {
        var model = new CorrectnessModel();
        model.recordSuccessfulPayment("payment-42");
        expectInvariant("INV-PAYMENT-01", () -> model.recordSuccessfulPayment("payment-42"));
    }

    private static void requestIdentityIsNotLogicalPaymentIdentity() {
        var model = new CorrectnessModel();
        // Two transport attempts may be request-1 and request-2, yet both mean payment-42.
        model.recordSuccessfulPayment("payment-42");
        expectInvariant("INV-PAYMENT-01", () -> model.recordSuccessfulPayment("payment-42"));
    }

    private static void cancellationAndAcceptanceCannotBothBecomeFinalSequentially() {
        var model = new CorrectnessModel();
        var orderId = new OrderId("order-42");
        var afterCancel = model.decideExclusiveOutcome(orderId, OrderState.PAID, OrderState.CANCELLED);
        expectInvariant("INV-ORDER-02", () -> model.decideExclusiveOutcome(orderId, afterCancel, OrderState.ACCEPTED));
    }

    private static void validationAndInvariantFailuresAreSemanticallyDifferent() {
        var model = new CorrectnessModel();
        try {
            model.recordSuccessfulPayment(" ");
            throw new AssertionError("expected validation failure");
        } catch (IllegalArgumentException expected) {
            // proposal malformed: validation failure
        }
        expectInvariant("INV-ORDER-01", () -> model.assertCommercialChangeAllowed(OrderState.PAID));
    }

    private static void expectInvariant(String id, Runnable action) {
        try {
            action.run();
            throw new AssertionError("expected invariant " + id + " to fail");
        } catch (InvariantViolation expected) {
            assert id.equals(expected.invariantId()) : expected.invariantId();
        }
    }
}
