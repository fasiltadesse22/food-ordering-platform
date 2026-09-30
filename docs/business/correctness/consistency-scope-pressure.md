# C2.1.12 — Consistency Scope Pressure

## Purpose
Expose the next correctness question after stale decisions: when a business rule depends on multiple facts, which facts must be evaluated and changed together, and what happens when they live in different independently mutable state components?

## Roadmap boundary
Cluster 1.1 requires reasoning about invariants, atomicity requirements, concurrency, and consistency implications. This part identifies the pressure; it deliberately does **not** choose aggregate boundaries, repositories, local ACID transaction design, optimistic locking, or cross-aggregate consistency mechanisms. Those are explicit Cluster 1.2 concerns.

## Core model

`invariant -> required facts -> owners of those facts -> mutation boundaries -> consistency pressure`

An invariant is local only when every correctness-relevant fact and effect needed to preserve it can be controlled inside one sufficiently strong consistency boundary. A rule that depends on independently changing facts creates cross-component consistency pressure.

## Example
Suppose restaurant acceptance requires both:
- the order lifecycle still permits acceptance; and
- payment is still established.

A decision can observe `(PAYMENT_ESTABLISHED, ESTABLISHED)`. If payment is independently changed to `REFUNDED` before acceptance becomes authoritative, the order-side decision may still look legal while the combined business condition is false.

## Important distinctions
- multiple fields != multiple consistency boundaries;
- multiple classes != distributed consistency;
- one process != one atomic transaction;
- one database != one transaction boundary;
- cross-component rule != requirement for microservices;
- consistency requirement != chosen consistency mechanism;
- aggregate boundary != service boundary.

## Architectural pressure
Before selecting a mechanism, identify:
1. the invariant;
2. every fact it depends on;
3. the authoritative owner of each fact;
4. who may mutate each fact;
5. which changes conflict;
6. the point at which the invariant must hold;
7. whether those facts/effects can be protected locally;
8. what coordination would be required if they cannot.

## Deliberately unresolved
- entity/value-object/aggregate modeling;
- aggregate roots and aggregate size;
- repository boundaries;
- database schema and persistence;
- local ACID transaction boundaries;
- optimistic/pessimistic concurrency;
- cross-aggregate consistency strategy;
- service decomposition;
- distributed transactions, Saga, Outbox, Kafka, Redis, locks, or eventual-consistency mechanisms.

These are later decisions. C2.1.12 exists so those mechanisms will be derived from correctness pressure rather than introduced by habit.
