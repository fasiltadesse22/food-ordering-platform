package com.foodordering.state;

import com.foodordering.domain.CustomerId;
import com.foodordering.domain.OrderId;
import com.foodordering.domain.RestaurantId;

import java.util.List;
import java.util.Objects;

/**
 * Part 1.1.4 state-representation lab.
 *
 * A snapshot answers "what values are represented for this Order identity at
 * this observation point?" It is not itself proof that the values are current
 * or authoritative. Lifecycle legality is deliberately deferred.
 */
public record OrderStateSnapshot(
        OrderId orderId,
        CustomerId customerId,
        RestaurantId restaurantId,
        List<String> requestedItems,
        boolean financialEffectEstablished,
        boolean restaurantAcceptanceEstablished,
        boolean cancellationEstablished,
        boolean preparationEstablished,
        boolean completionEstablished
) {
    public OrderStateSnapshot {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(restaurantId, "restaurantId");
        requestedItems = List.copyOf(Objects.requireNonNull(requestedItems, "requestedItems"));
        if (requestedItems.isEmpty()) throw new IllegalArgumentException("state requires at least one requested item");
        if (requestedItems.stream().anyMatch(i -> i == null || i.isBlank())) {
            throw new IllegalArgumentException("requested items must be named");
        }
    }

    public OrderStateSnapshot withFinancialEffectEstablished() {
        return new OrderStateSnapshot(orderId, customerId, restaurantId, requestedItems,
                true, restaurantAcceptanceEstablished, cancellationEstablished,
                preparationEstablished, completionEstablished);
    }

    public OrderStateSnapshot withRestaurantAcceptanceEstablished() {
        return new OrderStateSnapshot(orderId, customerId, restaurantId, requestedItems,
                financialEffectEstablished, true, cancellationEstablished,
                preparationEstablished, completionEstablished);
    }

    public OrderStateSnapshot withCancellationEstablished() {
        return new OrderStateSnapshot(orderId, customerId, restaurantId, requestedItems,
                financialEffectEstablished, restaurantAcceptanceEstablished, true,
                preparationEstablished, completionEstablished);
    }
}
