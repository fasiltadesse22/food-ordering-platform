package com.foodordering.lifecycle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Executable transition policy for the ordering lifecycle.
 *
 * Part 1.1.5 intentionally models sequential lifecycle legality only. It does
 * not provide atomicity, concurrency control, persistence, retries, or refund
 * orchestration. Those are separate correctness problems.
 */
public final class OrderLifecycle {
    private static final Map<OrderLifecycleState, Map<OrderTransition, OrderLifecycleState>> LEGAL = legalTransitions();

    public TransitionDecision decide(OrderLifecycleState current, OrderTransition requested) {
        Objects.requireNonNull(current, "current");
        Objects.requireNonNull(requested, "requested");

        var next = LEGAL.getOrDefault(current, Map.of()).get(requested);
        if (next != null) return new TransitionDecision.Allowed(current, requested, next);

        var reason = current.isTerminalForOrderingLifecycle()
                ? "ordering lifecycle is terminal in " + current
                : "transition " + requested + " is not legal from " + current;
        return new TransitionDecision.Rejected(current, requested, reason);
    }

    public OrderLifecycleState requireAllowed(OrderLifecycleState current, OrderTransition requested) {
        return switch (decide(current, requested)) {
            case TransitionDecision.Allowed allowed -> allowed.to();
            case TransitionDecision.Rejected rejected -> throw new IllegalStateException(rejected.reason());
        };
    }

    private static Map<OrderLifecycleState, Map<OrderTransition, OrderLifecycleState>> legalTransitions() {
        var transitions = new EnumMap<OrderLifecycleState, Map<OrderTransition, OrderLifecycleState>>(OrderLifecycleState.class);
        transitions.put(OrderLifecycleState.CREATED, Map.of(
                OrderTransition.ESTABLISH_PAYMENT, OrderLifecycleState.PAYMENT_ESTABLISHED,
                OrderTransition.CANCEL_ORDER, OrderLifecycleState.CANCELLED));
        transitions.put(OrderLifecycleState.PAYMENT_ESTABLISHED, Map.of(
                OrderTransition.ACCEPT_BY_RESTAURANT, OrderLifecycleState.RESTAURANT_ACCEPTED,
                OrderTransition.REJECT_BY_RESTAURANT, OrderLifecycleState.RESTAURANT_REJECTED,
                OrderTransition.CANCEL_ORDER, OrderLifecycleState.CANCELLED));
        transitions.put(OrderLifecycleState.RESTAURANT_ACCEPTED, Map.of(
                OrderTransition.START_PREPARATION, OrderLifecycleState.PREPARING,
                OrderTransition.CANCEL_ORDER, OrderLifecycleState.CANCELLED));
        transitions.put(OrderLifecycleState.PREPARING, Map.of(
                OrderTransition.COMPLETE_ORDER, OrderLifecycleState.COMPLETED));
        return Map.copyOf(transitions);
    }
}
