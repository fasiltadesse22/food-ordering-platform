package com.foodordering.correctness;

import com.foodordering.lifecycle.OrderLifecycle;
import com.foodordering.lifecycle.OrderLifecycleState;
import com.foodordering.lifecycle.OrderTransition;
import com.foodordering.lifecycle.TransitionDecision;

/**
 * C2.1.13 semantic experiment. It contrasts an uncoordinated conflicting
 * schedule with a minimal correctness coordination scope and an intentionally
 * over-broad scope. It does not select a production locking/transaction mechanism.
 */
public final class CoordinationCostPressureTest {
    public static void main(String[] args) {
        uncoordinatedConflictCanEscapeInvariant();
        minimalCoordinationSerializesTheConflict();
        broadCoordinationIncludesUnrelatedWork();
        System.out.println("PASS CoordinationCostPressureTest (3 tests)");
    }

    private static void uncoordinatedConflictCanEscapeInvariant() {
        var lifecycle = new OrderLifecycle();
        var state = new MutableTruth(OrderLifecycleState.PAYMENT_ESTABLISHED, PaymentFact.ESTABLISHED);

        var delayedAcceptance = (TransitionDecision.Allowed) lifecycle.decide(
                state.order, OrderTransition.ACCEPT_BY_RESTAURANT);
        assert combinedAcceptanceAllowed(lifecycle, state);

        state.payment = PaymentFact.REFUNDED;
        state.order = delayedAcceptance.to();

        assert state.order == OrderLifecycleState.RESTAURANT_ACCEPTED;
        assert state.payment == PaymentFact.REFUNDED;
    }

    private static void minimalCoordinationSerializesTheConflict() {
        var lifecycle = new OrderLifecycle();
        var state = new MutableTruth(OrderLifecycleState.PAYMENT_ESTABLISHED, PaymentFact.ESTABLISHED);
        var scope = new CoordinationScope();

        scope.enter();
        assert combinedAcceptanceAllowed(lifecycle, state);
        var acceptance = (TransitionDecision.Allowed) lifecycle.decide(
                state.order, OrderTransition.ACCEPT_BY_RESTAURANT);
        state.order = acceptance.to();
        scope.exit();

        // Refund is a conflicting operation and must enter after acceptance has
        // completed in this simple serial model. A later domain rule would decide
        // whether refund itself is legal after acceptance; this part only exposes
        // the coordination requirement/cost.
        scope.enter();
        state.payment = PaymentFact.REFUNDED;
        scope.exit();

        assert scope.entries == 2;
        assert state.order == OrderLifecycleState.RESTAURANT_ACCEPTED;
    }

    private static void broadCoordinationIncludesUnrelatedWork() {
        var scope = new CoordinationScope();

        scope.enter(); // acceptance/refund correctness work
        scope.exit();
        scope.enter(); // unrelated read-model formatting work
        scope.exit();

        assert scope.entries == 2;
        assert scope.unnecessaryEntriesForAcceptancePaymentInvariant() == 1;
    }

    private static boolean combinedAcceptanceAllowed(OrderLifecycle lifecycle, MutableTruth state) {
        return lifecycle.decide(state.order, OrderTransition.ACCEPT_BY_RESTAURANT)
                        instanceof TransitionDecision.Allowed
                && state.payment == PaymentFact.ESTABLISHED;
    }

    private enum PaymentFact { ESTABLISHED, REFUNDED }

    private static final class MutableTruth {
        private OrderLifecycleState order;
        private PaymentFact payment;

        private MutableTruth(OrderLifecycleState order, PaymentFact payment) {
            this.order = order;
            this.payment = payment;
        }
    }

    private static final class CoordinationScope {
        private int entries;

        private void enter() { entries++; }
        private void exit() { }

        private int unnecessaryEntriesForAcceptancePaymentInvariant() {
            return Math.max(0, entries - 1);
        }
    }
}
