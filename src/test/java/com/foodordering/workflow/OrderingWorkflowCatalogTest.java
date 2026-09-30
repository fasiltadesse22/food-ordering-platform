package com.foodordering.workflow;

import static com.foodordering.workflow.StepKind.*;

public final class OrderingWorkflowCatalogTest {
    public static void main(String[] args) {
        happyPathContainsDecisionAndOutcome();
        rejectionAfterFinancialStepPreservesUnresolvedCompensationPolicy();
        cancellationScenarioDoesNotInventConcurrencyPolicy();
        alternativeSequencingRemainsExplicitlyUndecided();
        System.out.println("PASS OrderingWorkflowCatalogTest (4 tests)");
    }

    private static void happyPathContainsDecisionAndOutcome() {
        var scenario = OrderingWorkflowCatalog.successfulFulfillment();
        require(scenario.contains(BUSINESS_DECISION), "happy path should expose business decisions");
        require(scenario.contains(BUSINESS_OUTCOME), "happy path should expose business outcomes");
    }

    private static void rejectionAfterFinancialStepPreservesUnresolvedCompensationPolicy() {
        var scenario = OrderingWorkflowCatalog.restaurantRejectsAfterFinancialStep();
        require(scenario.contains(UNRESOLVED_POLICY), "financial reversal policy must remain explicit rather than guessed");
        require(scenario.steps().stream().anyMatch(s -> s.description().contains("reversal or release")),
                "scenario should expose the financial consequence of rejection");
    }

    private static void cancellationScenarioDoesNotInventConcurrencyPolicy() {
        var scenario = OrderingWorkflowCatalog.cancellationAfterFinancialStepBeforeRestaurantDecision();
        require(scenario.steps().stream().filter(s -> s.kind() == UNRESOLVED_POLICY).count() >= 2,
                "cancellation scenario should preserve unresolved legality and financial consequences");
    }

    private static void alternativeSequencingRemainsExplicitlyUndecided() {
        var scenarios = OrderingWorkflowCatalog.discoveredScenarios();
        require(scenarios.stream().anyMatch(s -> s.name().equals("restaurant-rejects-before-financial-step")),
                "restaurant-first alternative should remain visible until product/payment semantics resolve sequencing");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
