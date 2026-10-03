# Cluster 1.1 Handoff — Business Workflows, State Transitions and Invariants

## What is now known
The project has explicit actors/responsibilities, workflow paths, command/decision/fact semantics, authoritative-state reasoning, an order lifecycle, invariant catalog, and controlled experiments for bypass, duplicate intent, conflicting operations, stale decisions, and cross-fact consistency pressure.

## Correctness requirements carried forward
- INV-ORDER-01: paid commercial agreement is not silently rewritten.
- INV-PAYMENT-01: at most one successful effect per logical payment.
- INV-ORDER-02: mutually exclusive final outcomes cannot both be authoritative.
- INV-REFUND-01: cumulative successful refund cannot exceed eligible captured value.
- Lifecycle legality must remain explicit.
- A decision whose correctness-relevant assumptions have changed must not silently become authoritative.
- When an invariant spans independently mutable facts, local legality alone is insufficient evidence of combined correctness.

## Unknowns deliberately preserved
- entity/value-object design;
- aggregate roots and aggregate size;
- repository boundaries;
- persistence schema;
- local ACID transaction boundaries;
- isolation level;
- optimistic/pessimistic concurrency control;
- cross-aggregate consistency strategy;
- service decomposition;
- distributed transaction/Saga strategy;
- Kafka, Outbox, Inbox, Redis, distributed locks;
- durable idempotency and restart recovery.

## Required next-cluster discipline
Cluster 1.2 must derive boundaries from the correctness evidence above. It must not erase the failure windows merely by introducing abstractions, and it must not claim guarantees stronger than the implementation and experiments establish.

## Supplemental artifact note
The repository contains a coordination-cost-pressure experiment created during an earlier numbering drift. It is retained as supplemental exploratory evidence, not as the authoritative scope of Part 1.1.13 and not as a selected production mechanism.
