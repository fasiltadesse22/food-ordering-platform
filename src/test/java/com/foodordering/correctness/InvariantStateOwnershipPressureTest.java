package com.foodordering.correctness;

import java.util.Set;
import java.util.stream.Collectors;

public final class InvariantStateOwnershipPressureTest {
    public static void main(String[] args) {
        everyInheritedInvariantNamesRequiredAuthoritativeFacts();
        paymentInvariantExposesCheckThenActPressure();
        orderOutcomeInvariantExposesSamePriorStateConflict();
        refundInvariantRequiresCombinedAccountingState();
        System.out.println("PASS InvariantStateOwnershipPressureTest (4 tests)");
    }

    private static void everyInheritedInvariantNamesRequiredAuthoritativeFacts() {
        var ids = InvariantStateDependencyCatalog.all().stream()
                .map(InvariantStateDependency::invariantId)
                .collect(Collectors.toSet());
        require(ids.equals(Set.of("INV-ORDER-01", "INV-PAYMENT-01", "INV-ORDER-02", "INV-REFUND-01")),
                "P03 must preserve all inherited invariant pressures");
        require(InvariantStateDependencyCatalog.all().stream()
                        .allMatch(i -> !i.requiredFacts().isEmpty() && !i.conflictingMutations().isEmpty()),
                "each invariant must expose facts and mutation conflict");
    }

    private static void paymentInvariantExposesCheckThenActPressure() {
        var payment = InvariantStateDependencyCatalog.oneSuccessfulPaymentEffect();
        require(payment.requiredFacts().stream().anyMatch(f -> f.name().contains("successful-effect")),
                "payment invariant needs current successful-effect truth");
        require(payment.forbiddenPartialStates().stream().anyMatch(s -> s.contains("two successful")),
                "forbidden outcome must be explicit");
    }

    private static void orderOutcomeInvariantExposesSamePriorStateConflict() {
        var outcome = InvariantStateDependencyCatalog.exclusiveOrderOutcome();
        require(outcome.conflictingMutations().stream()
                        .anyMatch(c -> c.first().contains("accept") && c.second().contains("cancel")),
                "accept/cancel conflict must remain visible");
    }

    private static void refundInvariantRequiresCombinedAccountingState() {
        var refund = InvariantStateDependencyCatalog.refundCeiling();
        var facts = refund.requiredFacts().stream().map(InvariantStateDependency.RequiredFact::name).toList();
        require(facts.contains("eligible captured value"), "refund needs eligible captured value");
        require(facts.contains("cumulative successful refunds"), "refund needs cumulative refund truth");
        require(facts.contains("proposed refund amount"), "refund needs proposed change");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
