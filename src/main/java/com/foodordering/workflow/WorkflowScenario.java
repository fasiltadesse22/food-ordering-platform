package com.foodordering.workflow;

import java.util.List;
import java.util.Objects;

/**
 * A discovered end-to-end business scenario. This is not a state machine and
 * does not claim that every policy question has been resolved.
 */
public record WorkflowScenario(String name, String actorGoal, List<WorkflowStep> steps) {
    public WorkflowScenario {
        name = requireText(name, "name");
        actorGoal = requireText(actorGoal, "actorGoal");
        steps = List.copyOf(Objects.requireNonNull(steps, "steps"));
        if (steps.isEmpty()) throw new IllegalArgumentException("workflow needs at least one step");
    }

    public boolean contains(StepKind kind) {
        return steps.stream().anyMatch(step -> step.kind() == kind);
    }

    private static String requireText(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) throw new IllegalArgumentException(name + " must not be blank");
        return value;
    }
}
