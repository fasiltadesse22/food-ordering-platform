# C2.1.11 Checkpoint

Part 1.1.11 is **Stale Decisions**.

## Predecessor
C2.1.10 at `ec806d8277258fb0417da89ffc5cbe64beb6ed8a`.

## Added
- explicit distinction between a stale read and a stale business decision;
- decision-validity reasoning across observation, delay, competing update and effect;
- deterministic cancellation-versus-delayed-acceptance experiment;
- evidence contrasting blind application of an old decision with reevaluation against current authoritative truth.

## Architectural conclusion
A decision is not made correct forever merely because it was correct when derived. Its justification depends on facts and preconditions. When correctness-relevant truth can change between observation and effect, the architecture needs a way to preserve, re-establish, or detect the invalidation of that relationship.

## Deliberately unresolved
- which concurrency-control mechanism should be selected;
- whether observation and effect require one atomic boundary;
- optimistic versioning, compare-and-set, pessimistic locking and transaction isolation;
- aggregate/repository and database transaction boundaries;
- real multithreaded/database contention;
- distributed stale-state and cross-service coordination;
- durable idempotency/deduplication and restart recovery;
- Kafka, Outbox, Inbox, Saga, Redis and distributed locks.

## Verification boundary
The new Java experiment is executable evidence. Runtime PASS is not claimed merely from repository publication; execute `scripts/verify.sh` with Java 21 to establish observed runtime evidence.
