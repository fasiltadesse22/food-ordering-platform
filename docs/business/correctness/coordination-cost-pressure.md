# C2.1.13 — Consistency Implications and the Cost of Coordination

## Purpose
Continue from C2.1.12's consistency-scope discovery by asking what architectural force appears when independently mutable facts must be kept sufficiently consistent for a business invariant.

## Core model

`invariant scope -> required agreement -> coordination scope -> serialization/waiting/failure coupling -> architectural cost`

A stronger or wider correctness requirement can require more coordination. Coordination is not free: it can reduce concurrency, introduce waiting, increase contention, couple availability to participants, and enlarge the failure/recovery surface. This does not mean coordination is bad; correctness requirements may make its cost necessary.

## Required reasoning
For each invariant ask:
1. Which facts must agree?
2. At what exact correctness point must they agree?
3. Which conflicting operations must not both become authoritative?
4. What degree of coordination would preserve that rule?
5. What concurrency is lost by that coordination?
6. What participants/resources become coupled?
7. What happens if a required participant is slow or unavailable?
8. Can the business invariant be narrowed or redesigned without weakening required correctness?

## Important distinctions
- correctness requirement != coordination mechanism;
- coordination != distributed transaction;
- strong consistency != global serialization;
- concurrency != correctness;
- atomicity != isolation;
- serialization of conflicting work != serialization of all work;
- availability cost != proof that eventual consistency is acceptable;
- temporary divergence is acceptable only when business semantics explicitly permit it.

## Architectural implication
The goal is not to minimize coordination at any cost. The goal is to coordinate the smallest correctness-relevant scope strongly enough to preserve required invariants while allowing unrelated work to proceed independently.

## Deliberately unresolved
This part does not choose aggregate boundaries, database transaction isolation, optimistic/pessimistic locking, distributed transactions, Saga, messaging, or service decomposition. Cluster 1.2 and later clusters will evaluate concrete mechanisms after the correctness and coordination forces are explicit.
