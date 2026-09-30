package com.foodordering.correctness;

import com.foodordering.lifecycle.OrderLifecycle;
import com.foodordering.lifecycle.OrderLifecycleState;
import com.foodordering.lifecycle.OrderTransition;
import com.foodordering.lifecycle.TransitionDecision;

/**
 * C2.1.12 controlled experiment: local transition legality can remain true
 * while a combined invariant depending on independently mutable facts becomes
 * false. This exposes consistency-scope pressure without selecting the later
 * aggregate, transaction, persistence, or distributed-coordination mechanism.
 */
public final class ConsistencyScopePressureTest {
    public static void main(String[] args) {
        localLegalityDoesNotProveCombinedInvariant();
        staleCombinedDecisionCanEscapeWithoutCrossFactProtection();
        System.out.println("PASS ConsistencyScopePressureTest (2 tests)");
    }

    private static void localLegalityDoesNotProveCombinedInvariant() {
        var lifecycle = new OrderLifecycle();
        var state = new MutableBusinessTruth(
                OrderLifecycleState.PAYMENT_ESTABLISHED,
                PaymentFact.ESTABLISHED);

        assert mayAccept(lifecycle, state);

        // Payment truth changes independently while order lifecycle state does not.
        state.payment = PaymentFact.REFUNDED;

        var orderOnlyDecision = lifecycle.decide(
                state.order,
                OrderTransition.ACCEPT_BY_RESTAURANT);

        assert orderOnlyDecision instanceof TransitionDecision.Allowed;
        assert !mayAccept(lifecycle, state);
    }

    private static void staleCombinedDecisionCanEscapeWithoutCrossFactProtection() {
        var lifecycle = new OrderLifecycle();
        var state = new MutableBusinessTruth(
                OrderLifecycleState.PAYMENT_ESTABLISHED,
                PaymentFact.ESTABLISHED);

        // T1/T2: decision is valid against both facts.
        assert mayAccept(lifecycle, state);
        var delayedOrderDecision = (TransitionDecision.Allowed) lifecycle.decide(
                state.order,
                OrderTransition.ACCEPT_BY_RESTAURANT);

        // T3: independently mutable payment fact invalidates the combined rule.
        state.payment = PaymentFact.REFUNDED;
        assert !mayAccept(lifecycle, state);

        // T7: blindly applying the old order result creates an impossible
        // combination according to the experiment's business invariant.
        state.order = delayedOrderDecision.to();
        assert state.order == OrderLifecycleState.RESTAURANT_ACCEPTED;
        assert state.payment == PaymentFact.REFUNDED;
    }

    private static boolean mayAccept(OrderLifecycle lifecycle, MutableBusinessTruth state) {
        var orderDecision = lifecycle.decide(
                state.order,
                OrderTransition.ACCEPT_BY_RESTAURANT);
        return orderDecision instanceof TransitionDecision.Allowed
                && state.payment == PaymentFact.ESTABLISHED;
    }

    private enum PaymentFact {
        ESTABLISHED,
        REFUNDED
    }

    private static final class MutableBusinessTruth {
        private OrderLifecycleState order;
        private PaymentFact payment;

        private MutableBusinessTruth(OrderLifecycleState order, PaymentFact payment) {
            this.order = order;
            this.payment = payment;
        }
    }
}
