package com.foodordering.workflow;

/** Semantic role of a step in a discovered business workflow. */
public enum StepKind {
    ACTOR_ACTION,
    BUSINESS_DECISION,
    BUSINESS_OUTCOME,
    UNRESOLVED_POLICY
}
