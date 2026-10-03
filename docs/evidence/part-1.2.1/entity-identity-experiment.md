# C2.1.2-P01 — Entity Identity Experiment

## Hypothesis
If Order is an Entity, business identity rather than descriptive equality determines whether two representations concern the same continuing business thing.

## Controlled cases

### Case A — same description, different identity
Create two Orders with the same customer, restaurant and requested items but different OrderIds.

Prediction: they are different Entities.

### Case B — same identity, changed description
Create one Order, change requested items, and observe its OrderId.

Prediction: identity remains unchanged.

### Case C — different Java objects, same business identity
Create two in-memory Order objects with the same OrderId but different represented item state.

Prediction: they represent the same business Entity even though they are not the same Java object.

### Case D — identity value versus Entity
Compare an OrderId value with the Order it identifies.

Prediction: they are not the same domain concept.

## Failure this exposes
A model based only on structural/descriptive equality could collapse two independent orders that happen to contain the same values. A model based only on Java object identity could treat two representations of the same Order as different business things.

## Evidence boundary
The test is deterministic in-process evidence of domain equality semantics. It is not evidence for persistence identity maps, ORM proxies, database uniqueness, concurrency, restart behavior or cross-service identity.

## Architectural pressure carried forward
Entity identity tells us what continues through change. It does not tell us which state must change atomically with that Entity. P03 must derive that from invariants before P04 proposes Aggregate boundaries.
