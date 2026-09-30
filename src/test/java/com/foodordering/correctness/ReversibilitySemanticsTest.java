package com.foodordering.correctness;

import java.util.ArrayList;
import java.util.List;

/**
 * C2.1.8 semantic experiment: compensation adds history; it does not erase
 * the original consequential fact.
 */
public final class ReversibilitySemanticsTest {
    public static void main(String[] args) {
        refundDoesNotEraseSuccessfulPaymentFact();
        compensationFailureLeavesOriginalEffectTrue();
        System.out.println("PASS ReversibilitySemanticsTest (2 tests)");
    }

    private static void refundDoesNotEraseSuccessfulPaymentFact() {
        var history = new BusinessHistory();
        history.record("PaymentSucceeded:payment-42:1000");
        history.record("CancellationAccepted:order-42");
        history.record("RefundSucceeded:payment-42:1000");

        assert history.contains("PaymentSucceeded:payment-42:1000");
        assert history.contains("RefundSucceeded:payment-42:1000");
        assert history.size() == 3;
    }

    private static void compensationFailureLeavesOriginalEffectTrue() {
        var history = new BusinessHistory();
        history.record("PaymentSucceeded:payment-42:1000");
        history.record("RefundRequested:payment-42:1000");
        history.record("RefundFailed:payment-42:provider-unavailable");

        assert history.contains("PaymentSucceeded:payment-42:1000");
        assert !history.contains("RefundSucceeded:payment-42:1000");
    }

    private static final class BusinessHistory {
        private final List<String> facts = new ArrayList<>();

        void record(String fact) { facts.add(fact); }
        boolean contains(String fact) { return facts.contains(fact); }
        int size() { return facts.size(); }
    }
}
