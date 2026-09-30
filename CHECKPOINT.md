# C2.1.7 Checkpoint

Part 1.1.7 is **Invariants: The Truths Architecture Must Protect**. It deepens the invariant model without choosing later architectural mechanisms prematurely.

## Inherited baseline
C2.1.6 introduced executable correctness claims for paid-order commercial immutability, logical-payment uniqueness, and mutually exclusive order outcomes.

## Added / refined
- invariant catalog now states scope, violations, enforcement status, and atomicity pressure;
- refund eligibility is recorded as a specification-level financial invariant, with policy details explicitly unresolved;
- atomicity requirement register maps invariants to competing operations and state that must be reasoned about together;
- bypass experiment demonstrates specification != enforcement and passing guarded-path tests != universal guarantee.

## Deliberately unresolved
- aggregate and repository boundaries;
- local ACID transaction design;
- optimistic/pessimistic concurrency control;
- durable idempotency/deduplication;
- concurrent cancel/accept and duplicate-payment race resolution;
- cross-service consistency;
- Kafka, Saga, Outbox, Inbox, Redis, CQRS, distributed locks.

## Verification boundary
The GitHub connector can create and inspect repository objects but does not execute Java. Runtime verification must be recorded only after `./scripts/verify.sh` is actually run in a Java 21 environment. Until then, the new bypass experiment is committed as prepared evidence, not a claimed runtime PASS.
