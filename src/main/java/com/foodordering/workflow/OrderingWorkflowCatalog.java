package com.foodordering.workflow;

import java.util.List;

import static com.foodordering.workflow.StepKind.*;

/**
 * Executable business-discovery artifact for Part 1.1.2.
 *
 * The catalog makes the happy path and important alternatives explicit while
 * preserving unresolved policy instead of inventing business rules.
 */
public final class OrderingWorkflowCatalog {
    private OrderingWorkflowCatalog() {}

    public static WorkflowScenario successfulFulfillment() {
        return new WorkflowScenario(
                "successful-fulfillment",
                "Customer obtains requested food from a restaurant",
                List.of(
                        step(ACTOR_ACTION, "Customer", "expresses ordering intent for one restaurant"),
                        step(BUSINESS_DECISION, "Platform/Business", "determines whether the ordering intent can enter the ordering workflow"),
                        step(ACTOR_ACTION, "Payment participant", "participates in the required financial step; exact authorization/capture semantics remain unresolved"),
                        step(BUSINESS_DECISION, "Restaurant", "decides whether it can and will fulfill the requested order"),
                        step(BUSINESS_OUTCOME, "Restaurant", "accepts the fulfillment commitment"),
                        step(ACTOR_ACTION, "Restaurant", "prepares the accepted order"),
                        step(BUSINESS_OUTCOME, "Platform/Business", "records the workflow as completed according to a completion definition still to be refined")
                ));
    }

    public static WorkflowScenario restaurantRejectsAfterFinancialStep() {
        return new WorkflowScenario(
                "restaurant-rejects-after-financial-step",
                "Customer receives a valid outcome even when the restaurant cannot fulfill",
                List.of(
                        step(ACTOR_ACTION, "Customer", "expresses ordering intent"),
                        step(ACTOR_ACTION, "Payment participant", "establishes some financial effect; exact semantics remain unresolved"),
                        step(BUSINESS_DECISION, "Restaurant", "decides it cannot or will not fulfill"),
                        step(BUSINESS_OUTCOME, "Restaurant", "rejects the requested fulfillment"),
                        step(UNRESOLVED_POLICY, "Platform/Business", "determine what financial reversal or release is required and what final order outcome should be established")
                ));
    }

    public static WorkflowScenario cancellationAfterFinancialStepBeforeRestaurantDecision() {
        return new WorkflowScenario(
                "cancellation-after-financial-step-before-restaurant-decision",
                "Customer attempts to stop an in-progress order without creating contradictory outcomes",
                List.of(
                        step(ACTOR_ACTION, "Customer", "expresses ordering intent"),
                        step(ACTOR_ACTION, "Payment participant", "participates in a financial step"),
                        step(ACTOR_ACTION, "Customer", "requests cancellation before the restaurant decision is known"),
                        step(UNRESOLVED_POLICY, "Platform/Business", "decide whether cancellation is legal at this point and how it interacts with a concurrent restaurant decision"),
                        step(UNRESOLVED_POLICY, "Platform/Business", "determine whether a financial reversal/release is required")
                ));
    }

    public static WorkflowScenario restaurantRejectsBeforeFinancialStep() {
        return new WorkflowScenario(
                "restaurant-rejects-before-financial-step",
                "Customer receives a rejection without unnecessary financial effect when ordering policy permits restaurant-first decision",
                List.of(
                        step(ACTOR_ACTION, "Customer", "expresses ordering intent"),
                        step(BUSINESS_DECISION, "Restaurant", "decides it cannot or will not fulfill"),
                        step(BUSINESS_OUTCOME, "Restaurant", "rejects the requested fulfillment"),
                        step(UNRESOLVED_POLICY, "Platform/Business", "decide whether this restaurant-first ordering is the intended product policy")
                ));
    }

    public static List<WorkflowScenario> discoveredScenarios() {
        return List.of(
                successfulFulfillment(),
                restaurantRejectsAfterFinancialStep(),
                cancellationAfterFinancialStepBeforeRestaurantDecision(),
                restaurantRejectsBeforeFinancialStep()
        );
    }

    private static WorkflowStep step(StepKind kind, String participant, String description) {
        return new WorkflowStep(kind, participant, description);
    }
}
