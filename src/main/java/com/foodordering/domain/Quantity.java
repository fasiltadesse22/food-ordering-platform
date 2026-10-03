package com.foodordering.domain;

/**
 * C2.1.2-P02 Value Object: a domain quantity, not an unqualified primitive.
 * Its meaning is entirely its value; it has no continuing identity or lifecycle.
 */
public record Quantity(int value) {
    public Quantity {
        if (value <= 0) throw new IllegalArgumentException("quantity must be greater than zero");
    }

    public Quantity plus(Quantity other) {
        if (other == null) throw new NullPointerException("other");
        return new Quantity(Math.addExact(value, other.value));
    }
}
