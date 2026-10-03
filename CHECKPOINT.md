# C2.1.2-P03 Checkpoint

Part 1.2.3 is **Invariant to Required State to Ownership Pressure**.

## Scope
For every inherited invariant, identify required authoritative facts, current representations, mutation capabilities, conflicting mutations, forbidden states, and atomicity/consistency pressure.

P03 does not select the later structural or persistence solution.

## Inherited baseline
C2.1.2-P02 remains the immediate baseline. P01 identity semantics, P02 value semantics, and all Cluster 1.1 lifecycle, invariant, and failure-window evidence are preserved.

## Added
- executable InvariantStateDependency learning model;
- dependency catalog for INV-ORDER-01, INV-PAYMENT-01, INV-ORDER-02, and INV-REFUND-01;
- explicit fact, authority, mutation, conflict, and forbidden-state reasoning;
- P03 experiment preserving the inherited stale cross-fact failure window;
- distinction between domain ownership pressure and deployment ownership;
- distinction between atomicity requirement and a future implementation mechanism.

## Verification status
Prepared but not runtime-observed in this environment. scripts/verify.sh registers InvariantStateOwnershipPressureTest and preserves ConsistencyScopePressureTest. Publication alone is not runtime PASS evidence.

## Architectural conclusion
P01 and P02 tell us what has identity and what is defined by value. P03 tells us which facts each invariant needs and which mutations can invalidate a decision. That creates evidence for comparing candidate consistency boundaries in P04, but does not choose one.

## Deliberately unresolved
- candidate and final consistency boundaries;
- mutation entry-point authority;
- repository, persistence, and schema design;
- local ACID transaction boundaries;
- optimistic or pessimistic concurrency;
- cross-boundary consistency mechanisms;
- service decomposition;
- distributed transaction, Saga, Kafka, Outbox, Redis, or locking mechanisms.

## Next part
P04 derives and compares candidate Aggregate designs from these invariant, fact, and mutation graphs. It must compare alternatives rather than assume one Aggregate per future service.
