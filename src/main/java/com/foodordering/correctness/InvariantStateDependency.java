package com.foodordering.correctness;

import java.util.List;
import java.util.Objects;

/**
 * C2.1.2-P03 learning model.
 *
 * Describes correctness dependencies without choosing an Aggregate, repository,
 * transaction, database or service boundary. "Owner" means the current
 * authoritative business-state responsibility in the learning model, not a
 * future deployment owner.
 */
public record InvariantStateDependency(
        String invariantId,
        String requiredTruth,
        List<RequiredFact> requiredFacts,
        List<ConflictingMutation> conflictingMutations,
        List<String> forbiddenPartialStates
) {
    public InvariantStateDependency {
        requireText(invariantId, "invariantId");
        requireText(requiredTruth, "requiredTruth");
        requiredFacts = List.copyOf(Objects.requireNonNull(requiredFacts, "requiredFacts"));
        conflictingMutations = List.copyOf(Objects.requireNonNull(conflictingMutations, "conflictingMutations"));
        forbiddenPartialStates = List.copyOf(Objects.requireNonNull(forbiddenPartialStates, "forbiddenPartialStates"));
        if (requiredFacts.isEmpty()) throw new IllegalArgumentException("invariant needs required facts");
    }

    public record RequiredFact(String name, String currentAuthority, String mutationCapability) {
        public RequiredFact {
            requireText(name, "name");
            requireText(currentAuthority, "currentAuthority");
            requireText(mutationCapability, "mutationCapability");
        }
    }

    public record ConflictingMutation(String first, String second, String conflictReason) {
        public ConflictingMutation {
            requireText(first, "first");
            requireText(second, "second");
            requireText(conflictReason, "conflictReason");
        }
    }

    private static void requireText(String value, String name) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(name + " must be present");
    }
}
