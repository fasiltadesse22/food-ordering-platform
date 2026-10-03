package com.foodordering.domain;

import java.util.Objects;

/**
 * Composite Value Object used only to make P02 value semantics executable.
 *
 * ItemName is represented as String for now: P02 does not invent a MenuItem
 * Entity or aggregate boundary. The whole line is defined by its component
 * values and is replaced rather than mutated.
 */
public record OrderLine(String itemName, Quantity quantity, Money unitPrice) {
    public OrderLine {
        Objects.requireNonNull(itemName, "itemName");
        if (itemName.isBlank()) throw new IllegalArgumentException("item name must be present");
        Objects.requireNonNull(quantity, "quantity");
        Objects.requireNonNull(unitPrice, "unitPrice");
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }

    public OrderLine withQuantity(Quantity replacement) {
        return new OrderLine(itemName, Objects.requireNonNull(replacement, "replacement"), unitPrice);
    }
}
