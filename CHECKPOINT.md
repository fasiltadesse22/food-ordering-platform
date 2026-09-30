# C2.1.12 Checkpoint

Part 1.1.12 is **Consistency Scope Pressure**.

## Predecessor
C2.1.11 at `976fdf1717be02861072ed2c12bb8fa655bd11ae`.

## Added
- explicit derivation from invariant scope to consistency-boundary pressure;
- distinction between local transition legality and a business invariant spanning multiple independently mutable facts;
- deterministic order/payment experiment exposing a combined invariant that becomes false while order-only validation remains legal;
- explicit boundary between Cluster 1.1 correctness discovery and Cluster 1.2 aggregate/local-transaction design.

## Architectural conclusion
Before choosing transactions, locks, aggregates, services, Saga, or messaging, identify every fact on which an invariant depends and the point at which those facts must agree. If correctness-relevant facts can change independently, the invariant creates coordination/consistency pressure. The appropriate boundary and mechanism remain design questions.

## Deliberately unresolved
- entity/value-object/aggregate design;
- aggregate roots and aggregate size;
- repositories and persistence;
- local ACID transaction boundaries;
- optimistic/pessimistic concurrency mechanisms;
- cross-aggregate consistency strategy;
- service decomposition and independent deployment;
- distributed transactions, Saga, Kafka, Outbox, Redis, distributed locks, or eventual-consistency mechanisms.

## Verification boundary
The new Java experiment is executable evidence prepared in the repository. Runtime PASS is not claimed from publication alone; execute `scripts/verify.sh` with Java 21 to establish observed runtime evidence.
