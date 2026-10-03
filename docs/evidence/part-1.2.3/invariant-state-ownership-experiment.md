# C2.1.2-P03 — Invariant State / Ownership Pressure Experiment

## Question
Can consistency pressure be derived from inherited invariants by naming required facts, current authority/mutation capability, conflicting mutations and forbidden states before selecting a structural or transaction mechanism?

## Hypothesis
If an invariant depends on facts/effects that can change independently, a locally legal decision or stale combination can still produce a forbidden combined business state.

## Prediction
All four inherited invariants expose required facts, a mutation conflict, a forbidden state and unresolved ownership/atomicity/consistency pressure.

## Setup
InvariantStateDependencyCatalog encodes the four inherited invariants as dependency descriptions. InvariantStateOwnershipPressureTest checks coverage of those facts/conflicts. The inherited ConsistencyScopePressureTest remains the dynamic failure-window experiment: payment truth changes after restaurant acceptance observes it.

## Controlled variable
No architectural enforcement mechanism is introduced. Only the explicitness of the dependency/mutation model changes.

## Expected observation
Local value validity and local transition legality are insufficient whenever the invariant depends on another independently mutable authoritative fact.

## Evidence classification
- Specified: inherited business invariants and forbidden outcomes.
- Predicted: each invariant exposes ownership/consistency pressure.
- Prepared: dependency catalog, P03 test and inherited dynamic pressure test.
- Observed: not claimed until verification actually executes.
- Inferred: later boundary design must account for these dependency and mutation graphs.
- Not proven: final boundaries, transaction atomicity/isolation, persistence, concurrency control, crash safety, restart recovery or distributed consistency.

## Limitation
This is a reasoning experiment. It intentionally cannot prove a future mechanism because P03 does not select one.
