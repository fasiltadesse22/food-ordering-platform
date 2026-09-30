package com.foodordering.correctness;

public final class InvariantViolation extends RuntimeException {
    private final String invariantId;

    public InvariantViolation(String invariantId, String message) {
        super(message);
        this.invariantId = invariantId;
    }

    public String invariantId() {
        return invariantId;
    }
}
