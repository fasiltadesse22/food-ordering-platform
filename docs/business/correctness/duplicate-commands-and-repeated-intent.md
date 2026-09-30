# C2.1.9 — Duplicate Commands and Repeated Intent

## Purpose
Model the correctness problem created when one logical business intent is represented by more than one command delivery or execution attempt.

## Core distinction
A command delivery/execution attempt is not the same thing as a logical business operation.

For one logical payment `payment-42`, the system may observe attempts `attempt-1`, `attempt-2`, and `attempt-3`. Transport/request identity may differ while business identity remains the same.

## Why duplicates arise
Repeated intent can be produced by user double-submit, client retry, intermediary retry, timeout ambiguity, process recovery, or later message redelivery. This part does not yet introduce networking or messaging infrastructure; it models the semantic pressure those mechanisms will later expose.

## Correctness classes

### Naturally repeatable operation
Repeating the operation is acceptable by business semantics, e.g. a pure query.

### Duplicate command with one allowed effect
Multiple deliveries represent the same logical intent, but the consequential business effect must occur at most once. `INV-PAYMENT-01` is the current example.

### Repeated command that may legitimately create new effects
Two commands that look similar can represent distinct business intents. Identity must distinguish them rather than suppressing them as duplicates.

## Current Food Ordering pressure
For a logical payment identity `P42`:

`attempt-1(P42)` and `attempt-2(P42)` may be distinct attempts but must not create two successful payment effects.

The current `CorrectnessModel` rejects sequential duplicate recording for the same logical payment ID. This demonstrates a semantic guard only. It is deliberately not a durable, concurrent, crash-safe, network-safe or distributed idempotency mechanism.

## Guarantees and non-guarantees
Current model demonstrates:
- sequential recognition of the same logical payment identity;
- rejection of a second successful-effect recording through the guarded path.

Current model does not guarantee:
- concurrency safety;
- durability across restart;
- atomicity with an external provider effect;
- safe retry after an ambiguous timeout;
- cross-process deduplication;
- exactly-once delivery or exactly-once processing.

## Architectural pressure
Before selecting idempotency keys, uniqueness constraints, operation records, inboxes, locks, transactions or provider protocols, establish:
1. what identifies the logical business operation;
2. which repeated executions are equivalent;
3. which effect must be bounded;
4. how long sameness must be remembered;
5. which participant is authoritative for that decision;
6. what failure/recovery windows can cause the intent to reappear.
