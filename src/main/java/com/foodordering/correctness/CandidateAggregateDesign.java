package com.foodordering.correctness;

import java.util.List;
import java.util.Objects;

/**
 * C2.1.2-P04 architectural learning model.
 *
 * A candidate Aggregate is a hypothesis about a consistency boundary, not a
 * final class, service, table, repository or deployment boundary.
 */
public record CandidateAggregateDesign(
        String name,
        List<String> consistencyUnits,
        List<String> invariantsLocalized,
        List<String> invariantsCrossingBoundary,
        List<String> strengths,
        List<String> risks,
        String status
) {
    public CandidateAggregateDesign {
        requireText(name, "name");
        consistencyUnits = copy(consistencyUnits, "consistencyUnits");
        invariantsLocalized = copy(invariantsLocalized, "invariantsLocalized");
        invariantsCrossingBoundary = copy(invariantsCrossingBoundary, "invariantsCrossingBoundary");
        strengths = copy(strengths, "strengths");
        risks = copy(risks, "risks");
        requireText(status, "status");
    }

    private static List<String> copy(List<String> values, String name) {
        Objects.requireNonNull(values, name);
        return List.copyOf(values);
    }

    private static void requireText(String value, String name) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(name + " must be present");
    }
}
