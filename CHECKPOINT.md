# C2.1.6 Checkpoint

Part 1.1.6 translates selected business truths into executable correctness claims while keeping the distinction between a business invariant and its enforcement mechanism explicit.

## Added
- invariant catalog with IDs and architectural implications;
- executable correctness model;
- tests for paid-order immutability, logical-payment uniqueness, and conflicting outcomes;
- evidence document distinguishing observed sequential behavior from unproven concurrency/durability guarantees.

## Deliberately unresolved
- thread-safe atomic check-and-change;
- database persistence and ACID transaction boundaries;
- optimistic/pessimistic concurrency control;
- durable idempotency/deduplication;
- aggregate boundaries and repositories;
- cross-service consistency;
- Kafka/Saga/Outbox/Redis.

These are intentionally deferred so later mechanisms are introduced because the correctness model creates architectural pressure for them.
