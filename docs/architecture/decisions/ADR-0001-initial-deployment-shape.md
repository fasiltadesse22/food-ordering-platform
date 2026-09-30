# ADR-0001: Begin with a structured single-deployable baseline

## Status
Accepted for the current learning state; explicitly reconsiderable.

## Context
Business workflows, invariants, consistency boundaries, scaling needs, fault-isolation needs, and organizational ownership are not yet sufficiently understood to justify distributed deployment boundaries.

## Forces
- keep domain discovery cheap to change
- preserve executable evidence and Git history
- avoid introducing network/operational failure before it is educationally justified
- keep business concepts distinguishable without pretending they are services

## Alternatives
1. Immediate microservices based on nouns/actors.
2. Structured single deployable.
3. Throwaway disconnected demos.

## Decision
Use the smallest structured single-deployable Java baseline. No distributed infrastructure is introduced in Part 1.1.1.

## Consequences
Positive: low operational complexity, cheap refactoring, clear learning history.
Negative: no independent deployment/scaling/fault isolation yet.

## Reconsideration triggers
Evidence from later parts/clusters showing justified consistency boundaries, independent scaling/deployment, failure isolation, team ownership, or communication needs.
