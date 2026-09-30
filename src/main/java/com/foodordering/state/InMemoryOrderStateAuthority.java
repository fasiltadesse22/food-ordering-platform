package com.foodordering.state;

import com.foodordering.domain.OrderId;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Educational single-process authority used to make "authoritative state"
 * concrete. It is transient, non-durable, non-concurrent and not a repository
 * abstraction. Those concerns are intentionally deferred.
 */
public final class InMemoryOrderStateAuthority {
    private final Map<OrderId, OrderStateSnapshot> currentById = new HashMap<>();

    public void establish(OrderStateSnapshot state) {
        Objects.requireNonNull(state, "state");
        if (currentById.putIfAbsent(state.orderId(), state) != null) {
            throw new IllegalStateException("state already exists for " + state.orderId());
        }
    }

    public OrderStateSnapshot current(OrderId orderId) {
        Objects.requireNonNull(orderId, "orderId");
        var state = currentById.get(orderId);
        if (state == null) throw new NoSuchElementException("unknown order " + orderId);
        return state;
    }

    public void replace(OrderStateSnapshot next) {
        Objects.requireNonNull(next, "next");
        if (!currentById.containsKey(next.orderId())) {
            throw new NoSuchElementException("cannot replace unknown order " + next.orderId());
        }
        currentById.put(next.orderId(), next);
    }
}
