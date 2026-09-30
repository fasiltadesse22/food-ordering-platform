package com.foodordering.workflow;

import java.util.Objects;

/**
 * A business-level workflow step. It deliberately carries no transport,
 * persistence, service, or transaction semantics.
 */
public record WorkflowStep(StepKind kind, String participant, String description) {
    public WorkflowStep {
        Objects.requireNonNull(kind, "kind");
        participant = requireText(participant, "participant");
        description = requireText(description, "description");
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return value;
    }
}
