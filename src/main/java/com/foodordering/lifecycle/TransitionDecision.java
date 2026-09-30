package com.foodordering.lifecycle;

import java.util.Objects;

public sealed interface TransitionDecision permits TransitionDecision.Allowed, TransitionDecision.Rejected {
    record Allowed(OrderLifecycleState from, OrderTransition transition, OrderLifecycleState to) implements TransitionDecision {
        public Allowed {
            Objects.requireNonNull(from, "from");
            Objects.requireNonNull(transition, "transition");
            Objects.requireNonNull(to, "to");
        }
    }

    record Rejected(OrderLifecycleState from, OrderTransition transition, String reason) implements TransitionDecision {
        public Rejected {
            Objects.requireNonNull(from, "from");
            Objects.requireNonNull(transition, "transition");
            if (reason == null || reason.isBlank()) throw new IllegalArgumentException("rejection requires a reason");
        }
    }
}
