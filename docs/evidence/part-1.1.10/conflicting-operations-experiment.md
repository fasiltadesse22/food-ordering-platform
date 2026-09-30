# Part 1.1.10 — Conflicting Concurrent Operations Experiment

## Question
Can two distinct operations both be individually legal when decided from the same observed state, yet produce a correctness problem when their execution windows overlap?

## Hypothesis
Yes. Sequential transition validation is insufficient when a decision is separated in time from the authoritative write. Two operations can both observe `PAYMENT_ESTABLISHED`, independently decide that acceptance/rejection is legal, and later write incompatible outcomes.

## Prediction
- if the second operation reevaluates after the first authoritative transition, an incompatible restaurant rejection after acceptance is rejected;
- if acceptance and rejection both decide from the original `PAYMENT_ESTABLISHED` snapshot, both decisions are locally allowed;
- if both stale decisions write without concurrency control, the later write can overwrite the earlier state, hiding an already-made conflicting decision.

## Setup
Use the inherited `OrderLifecycle` unchanged. Add a test-only mutable state holder so the experiment can explicitly separate observation/decision from write. Do not add production locking, versioning, transactions, or database infrastructure.

## Controlled interleaving
T0 shared state = PAYMENT_ESTABLISHED
T1 accept path reads PAYMENT_ESTABLISHED
T2 reject path reads PAYMENT_ESTABLISHED
T3 accept path decides ALLOWED -> RESTAURANT_ACCEPTED
T4 reject path decides ALLOWED -> RESTAURANT_REJECTED
T5 accept path writes RESTAURANT_ACCEPTED
T6 reject path writes RESTAURANT_REJECTED using its stale decision

## What each participant knows
At T3 the accept path knows only that acceptance was legal against its observed snapshot.
At T4 the reject path knows only that rejection was legal against its observed snapshot.
Neither local decision proves that its precondition remains authoritative at T5/T6.

## Expected evidence
`ConflictingConcurrentOperationsTest` demonstrates the sequential reevaluation case, the two-locally-valid-decisions case, and the stale overwrite case.

## Interpretation
The failure is not that the transition table is wrong. The failure is that check/decision and authoritative state change are separate, allowing the truth used by a decision to become stale.

## Limitations
This is a deterministic interleaving model, not a real multithreaded/database race. It does not measure contention or prove any production concurrency mechanism. Those mechanisms remain deferred until their architectural force is established.

## Evidence classification
Prepared executable evidence only until run in Java 21. No runtime PASS is claimed by the GitHub connector.
