# C2.1.8 Checkpoint

Part 1.1.8 is **Reversible, Irreversible and Compensatable Business Actions**.

## Predecessor
C2.1.7 at `8f5b696d5840274b4115cee0f3f6e266d7267482`.

## Added
- explicit reversible / irreversible / compensatable vocabulary;
- Food Ordering action classification for payment, restaurant acceptance, preparation, cancellation and refund;
- semantic experiment showing that refund compensation adds history rather than erasing payment success;
- failure scenario showing that failed compensation leaves the original consequential effect true and creates unresolved work.

## Architectural conclusion
Rollback is not a universal business concept. Consequential external facts must be preserved; compensation is a new operation with its own eligibility, failure and correctness semantics.

## Deliberately unresolved
- concurrency/race resolution;
- aggregate and repository boundaries;
- local ACID transaction design;
- optimistic/pessimistic concurrency control;
- durable idempotency/deduplication;
- payment-provider integration and durable refund processing;
- cross-service consistency;
- Kafka, Saga, Outbox, Inbox, Redis, CQRS, Event Sourcing and distributed locks.

## Verification boundary
The GitHub connector does not execute Java. The new experiment is committed as prepared executable evidence. Runtime PASS must not be claimed until `./scripts/verify.sh` is executed in a Java 21 environment.
