package com.foodordering.semantics;

public final class OrderingSemanticsTest {
    private static int tests;

    public static void main(String[] args) {
        commandIsIntentNotFact();
        acceptedDecisionEstablishesFact();
        rejectedDecisionEstablishesNoSuccessFact();
        unresolvedPolicyIsNotInvented();
        repeatedCommandDoesNotByItselfProveIdempotency();
        System.out.println("PASS OrderingSemanticsTest (" + tests + " tests)");
    }

    private static void commandIsIntentNotFact() {
        var command = OrderingSemantics.requestCancellation("order-1", "customer-1");
        check(command.name().equals("RequestCancellation"), "intent should be explicit");
        check(!(command instanceof Object && command.getClass().equals(BusinessFact.class)), "command must not be a fact");
        tests++;
    }

    private static void acceptedDecisionEstablishesFact() {
        var command = OrderingSemantics.requestCancellation("order-2", "customer-1");
        var decision = OrderingSemantics.decideCancellation(command, true);
        check(decision instanceof Decision.Accepted, "legal cancellation should be accepted in the lab");
        var accepted = (Decision.Accepted) decision;
        check(accepted.establishedFacts().size() == 1, "accepted decision should establish one modeled fact");
        check(accepted.establishedFacts().getFirst().name().equals("OrderCancellationAccepted"), "fact should describe established outcome");
        tests++;
    }

    private static void rejectedDecisionEstablishesNoSuccessFact() {
        var command = OrderingSemantics.requestRestaurantAcceptance("order-3", "restaurant-1");
        var decision = OrderingSemantics.decideRestaurantAcceptance(command, false);
        check(decision instanceof Decision.Rejected, "business rejection is a valid decision result");
        tests++;
    }

    private static void unresolvedPolicyIsNotInvented() {
        var command = OrderingSemantics.requestCancellation("order-4", "customer-1");
        var decision = OrderingSemantics.decideCancellationWithoutLifecyclePolicy(command);
        check(decision instanceof Decision.Unresolved, "missing lifecycle policy must remain explicit");
        tests++;
    }

    private static void repeatedCommandDoesNotByItselfProveIdempotency() {
        var command = OrderingSemantics.requestCancellation("order-5", "customer-1");
        var first = OrderingSemantics.decideCancellation(command, true);
        var second = OrderingSemantics.decideCancellation(command, true);
        check(first instanceof Decision.Accepted && second instanceof Decision.Accepted,
                "this stateless lab repeats evaluation; idempotency has not been established");
        tests++;
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
