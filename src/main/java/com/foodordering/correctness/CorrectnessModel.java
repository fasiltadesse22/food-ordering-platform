package com.foodordering.correctness;

import com.foodordering.domain.OrderId;
import com.foodordering.lifecycle.OrderLifecycle;
import com.foodordering.lifecycle.OrderState;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * C2.1.6 executable correctness model.
 *
 * This class deliberately makes selected business truths executable without
 * pretending that a HashSet is a database, that a method is a transaction,
 * or that this model is safe under concurrent access. Those mechanisms belong
 * to later learning once their architectural force has been established.
 */
public final class CorrectnessModel {
    private final Set<String> successfulLogicalPayments = new HashSet<>();

    public void assertCommercialChangeAllowed(OrderState currentState) {
        Objects.requireNonNull(currentState, "currentState");
        if (currentState == OrderState.PAID
                || currentState == OrderState.ACCEPTED
                || currentState == OrderState.PREPARING
                || currentState == OrderState.READY
                || currentState == OrderState.OUT_FOR_DELIVERY
                || currentState == OrderState.DELIVERED
                || currentState == OrderState.REFUND_PENDING
                || currentState == OrderState.REFUNDED) {
            throw new InvariantViolation("INV-ORDER-01", "paid commercial terms cannot be silently changed");
        }
    }

    /**
     * Records one successful effect for a logical payment identity.
     * Sequential duplicate attempts are rejected. This is not a concurrency
     * guarantee: contains/add is intentionally left as a check-then-act window.
     */
    public void recordSuccessfulPayment(String logicalPaymentId) {
        if (logicalPaymentId == null || logicalPaymentId.isBlank()) {
            throw new IllegalArgumentException("logicalPaymentId must be present");
        }
        if (successfulLogicalPayments.contains(logicalPaymentId)) {
            throw new InvariantViolation("INV-PAYMENT-01", "duplicate successful payment effect");
        }
        successfulLogicalPayments.add(logicalPaymentId);
    }

    public OrderState decideExclusiveOutcome(OrderId orderId, OrderState current, OrderState requested) {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(requested, "requested");
        if (!OrderLifecycle.canTransition(current, requested)) {
            throw new InvariantViolation("INV-ORDER-02", "conflicting or illegal final outcome: " + current + " -> " + requested);
        }
        return requested;
    }
}
