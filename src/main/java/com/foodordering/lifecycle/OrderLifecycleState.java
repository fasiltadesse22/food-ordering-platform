package com.foodordering.lifecycle;

/**
 * Business-significant states for the ordering lifecycle discovered in Part 1.1.5.
 *
 * These are not database statuses and do not yet define an aggregate boundary.
 */
public enum OrderLifecycleState {
    CREATED(false),
    PAYMENT_ESTABLISHED(false),
    RESTAURANT_ACCEPTED(false),
    PREPARING(false),
    COMPLETED(true),
    CANCELLED(true),
    RESTAURANT_REJECTED(true);

    private final boolean terminalForOrderingLifecycle;

    OrderLifecycleState(boolean terminalForOrderingLifecycle) {
        this.terminalForOrderingLifecycle = terminalForOrderingLifecycle;
    }

    public boolean isTerminalForOrderingLifecycle() {
        return terminalForOrderingLifecycle;
    }
}
