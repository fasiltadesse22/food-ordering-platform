package com.foodordering.domain;

import java.util.List;
import java.util.Objects;

/**
 * Minimal executable representation of customer ordering intent.
 * This is deliberately NOT classified as an aggregate/entity in Part 1.1.1.
 */
public record OrderIntent(OrderId orderId, CustomerId customerId, RestaurantId restaurantId, List<String> requestedItems) {
    public OrderIntent {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(restaurantId, "restaurantId");
        requestedItems = List.copyOf(Objects.requireNonNull(requestedItems, "requestedItems"));
        if (requestedItems.isEmpty()) throw new IllegalArgumentException("An ordering intent needs at least one requested item");
        if (requestedItems.stream().anyMatch(i -> i == null || i.isBlank())) throw new IllegalArgumentException("Requested items must be named");
    }
}
