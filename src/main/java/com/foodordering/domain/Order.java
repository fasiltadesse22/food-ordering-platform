package com.foodordering.domain;

import java.util.List;
import java.util.Objects;

/**
 * C2.1.2-P01 learning model for Entity identity continuity.
 *
 * This is deliberately NOT yet an Aggregate Root. It makes one claim:
 * an Order is a continuing business thing identified by OrderId even when
 * descriptive state changes. Aggregate membership, transaction boundaries,
 * persistence and concurrency control remain unresolved.
 */
public final class Order {
    private final OrderId id;
    private CustomerId customerId;
    private RestaurantId restaurantId;
    private List<String> requestedItems;

    public Order(OrderId id, CustomerId customerId, RestaurantId restaurantId, List<String> requestedItems) {
        this.id = Objects.requireNonNull(id, "id");
        this.customerId = Objects.requireNonNull(customerId, "customerId");
        this.restaurantId = Objects.requireNonNull(restaurantId, "restaurantId");
        replaceRequestedItems(requestedItems);
    }

    public OrderId id() { return id; }
    public CustomerId customerId() { return customerId; }
    public RestaurantId restaurantId() { return restaurantId; }
    public List<String> requestedItems() { return requestedItems; }

    /**
     * Demonstrates identity continuity across descriptive change.
     * This method is NOT permission to change paid commercial terms:
     * Cluster 1.1's invariant remains authoritative and aggregate-level
     * enforcement is intentionally deferred.
     */
    public void replaceRequestedItems(List<String> items) {
        var copy = List.copyOf(Objects.requireNonNull(items, "items"));
        if (copy.isEmpty()) throw new IllegalArgumentException("order needs at least one requested item");
        if (copy.stream().anyMatch(i -> i == null || i.isBlank())) {
            throw new IllegalArgumentException("requested items must be named");
        }
        this.requestedItems = copy;
    }

    /**
     * Entity equality follows business identity, not current descriptive state.
     */
    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof Order order && id.equals(order.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
