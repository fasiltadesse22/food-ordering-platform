# Part 1.1.7 — Invariant Bypass Experiment

## Question
Does the existence of an invariant guard and passing tests prove that the invariant is globally enforced?

## Hypothesis
No. If another state-changing path can bypass the guard, the business invariant can still be violated even though guarded-path tests pass.

## Prediction
1. Calling the C2.1.6 paid-order guard with `PAID` will reject the mutation.
2. A deliberately unsafe representation that mutates quantity directly will still be able to represent `PAID + changed quantity` because no architectural boundary forces all changes through the guard.

## Setup
- Reuse `CorrectnessModel.assertCommercialChangeAllowed`.
- Add a test-only `UnsafeOrderProjection` with directly mutable quantity.
- Do not add persistence, aggregates, repositories, transactions, locks, or distributed infrastructure.

## Controlled variable
Mutation path: guarded versus bypass.

## Constants
Same business truth: INV-ORDER-01 — paid commercial terms cannot be silently rewritten.

## Expected observation
The guarded path rejects; the bypass path can represent the forbidden state.

## Interpretation
An invariant is a specification. A guard is one enforcement mechanism at one boundary. Correctness requires architecture to make all relevant authoritative mutations obey an enforcement strategy with sufficient atomicity and consistency.

## What this experiment does not prove
It does not establish the correct aggregate boundary, transaction mechanism, concurrency strategy, database constraint, or distributed consistency model. Those decisions are intentionally deferred.

## Evidence classification
- Observed after runtime execution: test output and assertions, if executed in a Java 21 environment.
- Inferred from code inspection: the bypass path does not invoke the guard.
- Assumed: the test-only representation stands in for a future accidental/alternate mutation path.
- Guaranteed: none beyond Java semantics and the explicitly tested execution conditions.
