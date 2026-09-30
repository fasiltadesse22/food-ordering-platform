package com.foodordering.correctness;

import com.foodordering.lifecycle.OrderLifecycle;
import com.foodordering.lifecycle.OrderLifecycleState;
import com.foodordering.lifecycle.OrderTransition;

/**
 * C2.1.10 controlled interleaving experiment.
 *
 * It intentionally separates read/decide from write so two individually legal
 * decisions can be made from the same stale state. This is evidence of the
 * race window, not a production concurrency-control mechanism.
 */
public final class ConflictingConcurrentOperationsTest {
    public static void main(String[] args) {
        sequentialReevaluationRejectsSecondConflict();
        staleReadDecideWriteAllowsTwoLocallyLegalDecisions();
        staleOverwriteCanHideTheLosingBusinessFact();
        System.out.println("PASS ConflictingConcurrentOperationsTest (3 tests)");
    }

    private static void sequentialReevaluationRejectsSecondConflict() {
        var lifecycle = new OrderLifecycle();
        var afterAccept = lifecycle.requireAllowed(
                OrderLifecycleState.PAYMENT_ESTABLISHED,
                OrderTransition.ACCEPT_BY_RESTAURANT);

        var cancelAfterAccept = lifecycle.decide(afterAccept, OrderTransition.CANCEL_ORDER);
        // Cancellation remains legal from RESTAURANT_ACCEPTED in the current
        // business model, so use restaurant rejection as the conflicting
        // mutually-exclusive decision after acceptance.
        var rejectAfterAccept = lifecycle.decide(afterAccept, OrderTransition.REJECT_BY_RESTAURANT);
        assert rejectAfterAccept instanceof com.foodordering.lifecycle.TransitionDecision.Rejected;
        assert cancelAfterAccept instanceof com.foodordering.lifecycle.TransitionDecision.Allowed;
    }

    private static void staleReadDecideWriteAllowsTwoLocallyLegalDecisions() {
        var lifecycle = new OrderLifecycle();
        var observedByAccept = OrderLifecycleState.PAYMENT_ESTABLISHED;
        var observedByReject = OrderLifecycleState.PAYMENT_ESTABLISHED;

        var accept = lifecycle.decide(observedByAccept, OrderTransition.ACCEPT_BY_RESTAURANT);
        var reject = lifecycle.decide(observedByReject, OrderTransition.REJECT_BY_RESTAURANT);

        assert accept instanceof com.foodordering.lifecycle.TransitionDecision.Allowed;
        assert reject instanceof com.foodordering.lifecycle.TransitionDecision.Allowed;
        // Both decisions are locally valid against the state each participant saw.
        // They cannot both be authoritative outcomes for one order.
    }

    private static void staleOverwriteCanHideTheLosingBusinessFact() {
        var lifecycle = new OrderLifecycle();
        var shared = new MutableOrderState(OrderLifecycleState.PAYMENT_ESTABLISHED);

        var acceptDecision = lifecycle.decide(shared.state, OrderTransition.ACCEPT_BY_RESTAURANT);
        var rejectDecision = lifecycle.decide(shared.state, OrderTransition.REJECT_BY_RESTAURANT);

        var accepted = (com.foodordering.lifecycle.TransitionDecision.Allowed) acceptDecision;
        var rejected = (com.foodordering.lifecycle.TransitionDecision.Allowed) rejectDecision;

        shared.state = accepted.to();   // first write
        shared.state = rejected.to();   // stale second write overwrites first

        assert shared.state == OrderLifecycleState.RESTAURANT_REJECTED;
        // Final state alone no longer reveals that an acceptance decision was
        // previously made. The experiment exposes lost-update/history pressure.
    }

    private static final class MutableOrderState {
        private OrderLifecycleState state;

        private MutableOrderState(OrderLifecycleState state) {
            this.state = state;
        }
    }
}
