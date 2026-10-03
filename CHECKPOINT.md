# C2.1.13 Checkpoint

Part 1.1.13 is **Correctness Evidence, Diagnosis and Staff-Level Design Defense**.

## Scope authority
This checkpoint follows the derived Cluster 1.1 blueprint for the V7.1 roadmap. It is the closing Type C evidence/synthesis part for Cluster 1.1.

## Inherited baseline
The cumulative repository already contains the workflow, semantic, authoritative-state, lifecycle, invariant, duplicate-intent, conflicting-operation, stale-decision, and consistency-scope artifacts produced by the preceding parts.

An earlier numbering drift temporarily labeled a supplemental coordination-cost experiment as C2.1.13. That experiment is preserved as exploratory material, but it is **not** the authoritative Part 1.1.13 scope and it does not select a production coordination mechanism.

## Added
- persistent Cluster 1.1 correctness evidence portfolio;
- production-style correctness diagnosis playbook;
- Staff-level architecture/design-defense artifact with adversarial follow-ups;
- explicit Cluster 1.1 -> Cluster 1.2 handoff;
- evidence classification separating specified, prepared, observed, inferred, and not-proven claims.

## Required evidence portfolio
The checkpoint explicitly covers:
- lifecycle and illegal transitions;
- duplicate logical payment intent/effect;
- cancellation/acceptance stale-decision race;
- conflicting mutually exclusive outcomes;
- paid-order modification/invariant bypass;
- cross-fact payment/order staleness, including payment change before delayed acceptance;
- refund correctness as a specified but not yet executable invariant.

## Architectural conclusion
Cluster 1.1 establishes **what must remain correct regardless of eventual architecture**. It does not yet choose the structural or infrastructure mechanisms that enforce those truths. Cluster 1.2 must consume this evidence to derive entity/value-object/aggregate ownership and local consistency boundaries.

## Deliberately unresolved
- aggregate/repository boundaries;
- persistence and database schema;
- local ACID transaction design and isolation level;
- optimistic/pessimistic concurrency control;
- service decomposition;
- distributed transaction/Saga design;
- Kafka, Outbox, Inbox, Redis, distributed locks;
- durable idempotency and restart recovery.

## Verification boundary
Existing executable experiments remain registered in `scripts/verify.sh`. This part primarily adds persistent evidence and design-defense artifacts; repository publication is not equivalent to observed Java runtime PASS.
