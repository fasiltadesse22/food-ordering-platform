# C2.1.10 — Conflicting Concurrent Operations and Race Windows

## Purpose
Expose how two distinct business operations can each be legal against the state they observe yet combine into an invalid or misleading outcome when read/decide/write windows overlap.

## Core model
Sequential correctness is not concurrent correctness.

For one order in `PAYMENT_ESTABLISHED`, restaurant acceptance and restaurant rejection are each individually legal. If two participants read the same initial state, decide independently, and then write without revalidation or coordination, both decisions can be locally justified even though one order cannot authoritatively be both accepted and rejected.

## Race window
`read state -> evaluate precondition -> decide -> write state`

The dangerous window is between observation/decision and authoritative state change. Another operation may change the relevant truth during that interval.

## Stale assumption
A precondition that was true when read is not automatically true when the decision is committed. Correctness therefore depends on the relationship between observation, decision, and state change—not merely on whether each command handler contains the right sequential validation.

## Current Food Ordering examples
- accept order || reject order;
- pay order || cancel order;
- cancel order || start preparation;
- refund || retry/re-establish a payment effect.

The exact business policy determines which pairs are conflicting. Concurrency itself is not an error; incompatible effects escaping together are the correctness problem.

## Guarantees and non-guarantees
The current lifecycle model demonstrates sequential transition legality. The C2.1.10 experiment demonstrates that independently evaluating two transitions against the same snapshot can create two locally allowed decisions and a stale overwrite.

It does not provide or claim:
- atomic compare-and-set;
- optimistic version checking;
- pessimistic locking;
- database transaction isolation;
- serialization;
- distributed locking;
- aggregate boundaries;
- cross-service coordination.

## Architectural pressure
Before selecting a concurrency-control mechanism, establish:
1. which operations truly conflict;
2. which state their preconditions depend on;
3. which outcomes are mutually exclusive;
4. what must be atomic;
5. what stale decisions must be detected or rejected;
6. whether conflicts are rare or frequent;
7. whether the invariant can be redesigned to reduce coordination.
