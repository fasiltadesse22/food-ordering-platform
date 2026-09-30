package com.foodordering.correctness;

import com.foodordering.lifecycle.OrderLifecycle;
import com.foodordering.lifecycle.OrderLifecycleState;
import com.foodordering.lifecycle.OrderTransition;
import com.foodordering.lifecycle.TransitionDecision;

/**
 * C2.1.11 controlled stale-decision experiment.
 *
 * A decision is derived from one authoritative truth, delayed, and then used
 * after a competing operation changes that truth. This exposes stale-decision
 * semantics without prematurely adding a production concurrency mechanism.
 */
public final class StaleDecisionExperimentTest {
    public static void main(String[] args) {
        decisionCanBeValidWhenDerivedAndInvalidWhenApplied();
        revalidationDetectsChangedPrecondition();
        System.out.println("PASS StaleDecisionExperimentTest (2 tests)");
    }

    private static void decisionCanBeValidWhenDerivedAndInvalidWhenApplied() {
        var lifecycle = new OrderLifecycle();
        var authoritative = new MutableOrderState(OrderLifecycleState.PAYMENT_ESTABLISHED);

        // T1/T2: restaurant observes current truth and derives a valid decision.
        var delayedAccept = lifecycle.decide(
                authoritative.state,
                OrderTransition.ACCEPT_BY_RESTAURANT);
        assert delayedAccept instanceof TransitionDecision.Allowed;
        var allowedAccept = (TransitionDecision.Allowed) delayedAccept;
        assert allowedAccept.to() == OrderLifecycleState.RESTAURANT_ACCEPTED;

        // T4: while acceptance is delayed, another operation changes truth.
        authoritative.state = lifecycle.requireAllowed(
                authoritative.state,
                OrderTransition.CANCEL_ORDER);
        assert authoritative.state == OrderLifecycleState.CANCELLED;

        // T6: current truth no longer permits the old business decision.
        var freshAccept = lifecycle.decide(
                authoritative.state,
                OrderTransition.ACCEPT_BY_RESTAURANT);
        assert freshAccept instanceof TransitionDecision.Rejected;

        // T7: a naive apply step can still make the historical result current.
        authoritative.state = allowedAccept.to();
        assert authoritative.state == OrderLifecycleState.RESTAURANT_ACCEPTED;
    }

    private static void revalidationDetectsChangedPrecondition() {
        var lifecycle = new OrderLifecycle();
        var authoritative = new MutableOrderState(OrderLifecycleState.PAYMENT_ESTABLISHED);

        var delayedAccept = lifecycle.decide(
                authoritative.state,
                OrderTransition.ACCEPT_BY_RESTAURANT);
        assert delayedAccept instanceof TransitionDecision.Allowed;

        authoritative.state = lifecycle.requireAllowed(
                authoritative.state,
                OrderTransition.CANCEL_ORDER);

        // Revalidation is demonstrated as a semantic contrast, not proposed as
        // a sufficient production concurrency-control implementation: another
        // race could exist between this check and a later write.
        var revalidated = lifecycle.decide(
                authoritative.state,
                OrderTransition.ACCEPT_BY_RESTAURANT);
        assert revalidated instanceof TransitionDecision.Rejected;
        assert authoritative.state == OrderLifecycleState.CANCELLED;
    }

    private static final class MutableOrderState {
        private OrderLifecycleState state;

        private MutableOrderState(OrderLifecycleState state) {
            this.state = state;
        }
    }
}
