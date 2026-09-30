package com.foodordering.semantics;

import java.util.List;

/**
 * Part 1.1.3 executable semantics lab. It demonstrates intent -> decision ->
 * fact without claiming to be the production Order model or lifecycle.
 */
public final class OrderingSemantics {
    private OrderingSemantics() {}

    public static BusinessCommand requestCancellation(String orderId, String customerId) {
        return new BusinessCommand("RequestCancellation", orderId, customerId);
    }

    public static BusinessCommand requestRestaurantAcceptance(String orderId, String restaurantId) {
        return new BusinessCommand("RequestRestaurantAcceptance", orderId, restaurantId);
    }

    public static Decision decideCancellation(BusinessCommand command, boolean cancellationKnownToBeLegal) {
        require(command, "RequestCancellation");
        if (!cancellationKnownToBeLegal) {
            return new Decision.Rejected("Cancellation is not legal under the supplied business condition");
        }
        return new Decision.Accepted(List.of(new BusinessFact(
                "OrderCancellationAccepted",
                command.targetId(),
                "The cancellation request was accepted as a business outcome"
        )));
    }

    public static Decision decideRestaurantAcceptance(BusinessCommand command, boolean restaurantCanFulfill) {
        require(command, "RequestRestaurantAcceptance");
        if (!restaurantCanFulfill) {
            return new Decision.Rejected("Restaurant cannot or will not fulfill the requested order");
        }
        return new Decision.Accepted(List.of(new BusinessFact(
                "RestaurantFulfillmentAccepted",
                command.targetId(),
                "The restaurant accepted the requested fulfillment"
        )));
    }

    public static Decision decideCancellationWithoutLifecyclePolicy(BusinessCommand command) {
        require(command, "RequestCancellation");
        return new Decision.Unresolved("Authoritative lifecycle/cancellation policy has not yet been modeled");
    }

    private static void require(BusinessCommand command, String expected) {
        if (!expected.equals(command.name())) throw new IllegalArgumentException("Expected " + expected);
    }
}
