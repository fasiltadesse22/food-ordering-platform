# C2.1.9 Checkpoint

Part 1.1.9 is **Duplicate Commands and Repeated Intent**.

## Predecessor
C2.1.8 at `949dcd2db3a4f2e6dbd653528682dc0d9d184c37`.

## Added
- explicit distinction between command/attempt identity and logical business-operation identity;
- classification of repeated operations by business semantics;
- controlled duplicate-intent experiment contrasting attempt-based and logical-payment identity;
- explicit guarantees and non-guarantees of the inherited sequential in-memory guard.

## Architectural conclusion
Duplicate correctness starts with identifying business equivalence. Different deliveries/attempts can represent one intent, while superficially similar commands can also represent distinct legitimate intents. Infrastructure-level IDs alone do not define business sameness.

## Deliberately unresolved
- conflicting concurrent operations;
- stale decisions;
- concurrency control;
- durable idempotency/deduplication;
- process-restart memory;
- external provider ambiguity;
- database uniqueness/transactions;
- aggregate/service boundaries;
- Kafka, Outbox, Inbox, Saga, Redis and distributed locks.

## Verification boundary
The GitHub connector does not execute Java. The new experiment is committed as prepared executable evidence. Runtime PASS must not be claimed until `./scripts/verify.sh` is executed in a Java 21 environment.
