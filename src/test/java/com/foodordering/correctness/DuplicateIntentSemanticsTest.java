package com.foodordering.correctness;

/**
 * C2.1.9 semantic experiment: transport/attempt identity may differ while
 * logical business identity remains the same.
 */
public final class DuplicateIntentSemanticsTest {
    public static void main(String[] args) {
        naiveAttemptIdentityAllowsDuplicateBusinessEffect();
        logicalIdentityRejectsSequentialDuplicateEffect();
        distinctLogicalPaymentsRemainDistinctBusinessIntents();
        System.out.println("PASS DuplicateIntentSemanticsTest (3 tests)");
    }

    private static void naiveAttemptIdentityAllowsDuplicateBusinessEffect() {
        var naive = new NaiveAttemptBasedRecorder();
        naive.record("attempt-1", "payment-42");
        naive.record("attempt-2", "payment-42");

        assert naive.successfulEffects == 2 : "different attempt IDs hide repeated logical intent";
    }

    private static void logicalIdentityRejectsSequentialDuplicateEffect() {
        var model = new CorrectnessModel();
        model.recordSuccessfulPayment("payment-42");

        try {
            model.recordSuccessfulPayment("payment-42");
            throw new AssertionError("expected INV-PAYMENT-01");
        } catch (InvariantViolation violation) {
            assert "INV-PAYMENT-01".equals(violation.invariantId());
        }
    }

    private static void distinctLogicalPaymentsRemainDistinctBusinessIntents() {
        var model = new CorrectnessModel();
        model.recordSuccessfulPayment("payment-42");
        model.recordSuccessfulPayment("payment-43");
    }

    private static final class NaiveAttemptBasedRecorder {
        private final java.util.Set<String> seenAttempts = new java.util.HashSet<>();
        private int successfulEffects;

        void record(String attemptId, String logicalPaymentId) {
            if (!seenAttempts.add(attemptId)) {
                throw new IllegalStateException("duplicate attempt");
            }
            // The bug: business equivalence is ignored. A new attempt ID is
            // treated as permission for another successful effect.
            successfulEffects++;
        }
    }
}
