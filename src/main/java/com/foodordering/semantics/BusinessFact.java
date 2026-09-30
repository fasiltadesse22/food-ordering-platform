package com.foodordering.semantics;

/**
 * A statement this learning model treats as established business reality.
 * This is deliberately not a messaging event contract.
 */
public record BusinessFact(String name, String subjectId, String description) {
    public BusinessFact {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("fact name is required");
        if (subjectId == null || subjectId.isBlank()) throw new IllegalArgumentException("subject id is required");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("description is required");
    }
}
