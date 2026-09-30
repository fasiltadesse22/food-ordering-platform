package com.foodordering.lifecycle;

public final class OrderLifecycleTest {
    private static int tests;
    private static final OrderLifecycle LIFECYCLE = new OrderLifecycle();

    public static void main(String[] args) {
        happyPathMovesForwardThroughBusinessStates();
        cancellationIsLegalBeforePreparationUnderCurrentAssumption();
        cancellationAfterPreparationIsIllegal();
        terminalStatesRejectFurtherOrderingTransitions();
        restaurantRejectionTerminatesOrderingButDoesNotClaimFinancialWorkIsFinished();
        stateMachineDoesNotPermitBackwardMovementAsRollback();
        System.out.println("PASS OrderLifecycleTest (" + tests + " tests)");
    }

    private static void happyPathMovesForwardThroughBusinessStates() {
        var state = OrderLifecycleState.CREATED;
        state = LIFECYCLE.requireAllowed(state, OrderTransition.ESTABLISH_PAYMENT);
        state = LIFECYCLE.requireAllowed(state, OrderTransition.ACCEPT_BY_RESTAURANT);
        state = LIFECYCLE.requireAllowed(state, OrderTransition.START_PREPARATION);
        state = LIFECYCLE.requireAllowed(state, OrderTransition.COMPLETE_ORDER);
        check(state == OrderLifecycleState.COMPLETED, "happy path should complete");
        tests++;
    }

    private static void cancellationIsLegalBeforePreparationUnderCurrentAssumption() {
        var state = LIFECYCLE.requireAllowed(OrderLifecycleState.CREATED, OrderTransition.ESTABLISH_PAYMENT);
        state = LIFECYCLE.requireAllowed(state, OrderTransition.ACCEPT_BY_RESTAURANT);
        state = LIFECYCLE.requireAllowed(state, OrderTransition.CANCEL_ORDER);
        check(state == OrderLifecycleState.CANCELLED, "accepted but not preparing order is cancellable in current lab policy");
        tests++;
    }

    private static void cancellationAfterPreparationIsIllegal() {
        var decision = LIFECYCLE.decide(OrderLifecycleState.PREPARING, OrderTransition.CANCEL_ORDER);
        check(decision instanceof TransitionDecision.Rejected, "preparing order should reject cancellation");
        tests++;
    }

    private static void terminalStatesRejectFurtherOrderingTransitions() {
        for (var terminal : new OrderLifecycleState[]{OrderLifecycleState.COMPLETED, OrderLifecycleState.CANCELLED, OrderLifecycleState.RESTAURANT_REJECTED}) {
            var decision = LIFECYCLE.decide(terminal, OrderTransition.CANCEL_ORDER);
            check(decision instanceof TransitionDecision.Rejected, terminal + " must reject further ordering transitions");
        }
        tests++;
    }

    private static void restaurantRejectionTerminatesOrderingButDoesNotClaimFinancialWorkIsFinished() {
        var state = LIFECYCLE.requireAllowed(OrderLifecycleState.PAYMENT_ESTABLISHED, OrderTransition.REJECT_BY_RESTAURANT);
        check(state == OrderLifecycleState.RESTAURANT_REJECTED, "restaurant rejection should terminate fulfillment progression");
        check(state.isTerminalForOrderingLifecycle(), "rejection is terminal for ordering lifecycle");
        // No refund state is asserted here: payment/refund consequences are a separate workflow responsibility.
        tests++;
    }

    private static void stateMachineDoesNotPermitBackwardMovementAsRollback() {
        var decision = LIFECYCLE.decide(OrderLifecycleState.RESTAURANT_ACCEPTED, OrderTransition.ESTABLISH_PAYMENT);
        check(decision instanceof TransitionDecision.Rejected, "business progress is not undone by replaying an earlier transition");
        tests++;
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
