# C2.1.10 Checkpoint

Part 1.1.10 is **Conflicting Concurrent Operations and Race Windows**.

## Predecessor
C2.1.9 at `32373c877b842e4c7d9758915eda49d123dc6cdf`.

## Added
- explicit distinction between sequential legality and concurrent correctness;
- read/decide/write race-window model and stale-precondition reasoning;
- controlled accept-versus-reject interleaving using the inherited lifecycle policy;
- evidence showing two locally legal decisions and a stale overwrite without prematurely adding concurrency control.

## Architectural conclusion
A command can be correct against the state it observed and still become unsafe before its effect is committed. Important invariants therefore create atomicity/consistency pressure around observation, decision and authoritative state change.

## Deliberately unresolved
- choice among serialization, optimistic versioning, pessimistic locking, transaction isolation, compare-and-set or workflow redesign;
- aggregate/repository and database transaction boundaries;
- real multithreaded/database contention behavior;
- distributed concurrency and cross-service coordination;
- durable idempotency/deduplication and restart recovery;
- Kafka, Outbox, Inbox, Saga, Redis and distributed locks.

## Verification boundary
The GitHub connector does not execute Java. The new experiment is committed as prepared executable evidence. Runtime PASS must not be claimed until `./scripts/verify.sh` is executed in a Java 21 environment.
