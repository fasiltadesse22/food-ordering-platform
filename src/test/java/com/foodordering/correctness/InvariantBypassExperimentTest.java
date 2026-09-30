package com.foodordering.correctness;

import com.foodordering.lifecycle.OrderState;

/**
 * C2.1.7 educational bypass experiment.
 *
 * The point is not to provide a production model. It demonstrates that having
 * an invariant-checking method does not make the invariant globally enforced:
 * another mutation path can simply fail to call the guard.
 */
public final class InvariantBypassExperimentTest {
    public static void main(String[] args) {
        guardedPathRejectsPaidCommercialMutation();
        unguardedPathCanCreateForbiddenBusinessState();
        System.out.println("PASS InvariantBypassExperimentTest (2 tests)");
    }

    private static void guardedPathRejectsPaidCommercialMutation() {
        var correctness = new CorrectnessModel();
        try {
            correctness.assertCommercialChangeAllowed(OrderState.PAID);
            throw new AssertionError("expected INV-ORDER-01");
        } catch (InvariantViolation expected) {
            assert "INV-ORDER-01".equals(expected.invariantId());
        }
    }

    private static void unguardedPathCanCreateForbiddenBusinessState() {
        var unsafe = new UnsafeOrderProjection(OrderState.PAID, 2);
        unsafe.quantity = 5; // bypasses CorrectnessModel entirely

        assert unsafe.state == OrderState.PAID;
        assert unsafe.quantity == 5;
        // This state is representable even though INV-ORDER-01 says it is forbidden.
        // Therefore the existence of a guard/test is evidence about a path, not a
        // universal guarantee about all state mutation.
    }

    private static final class UnsafeOrderProjection {
        private final OrderState state;
        private int quantity;

        private UnsafeOrderProjection(OrderState state, int quantity) {
            this.state = state;
            this.quantity = quantity;
        }
    }
}
