# Part 1.1.8 — Reversibility and Compensation Semantics Experiment

## Question
When a consequential action is compensated, does the original business fact disappear?

## Hypothesis
No. Compensation should be modeled as a new business action/fact. A successful payment remains historically true even after a successful refund.

## Prediction
1. A history containing `PaymentSucceeded`, cancellation and `RefundSucceeded` retains all three facts.
2. If refund compensation fails, `PaymentSucceeded` remains true and the system is left with unresolved financial work rather than an implicit rollback.

## Setup
Use a test-only append-only fact history. This is a semantic experiment, not Event Sourcing and not a persistence design.

## Expected observations
- successful refund adds a new fact without deleting `PaymentSucceeded`;
- failed refund leaves the original payment fact intact;
- therefore compensation has its own success/failure lifecycle and cannot be treated as a language/database rollback of the original external effect.

## Architectural pressure revealed
Later architecture must represent unresolved compensation, retries/recovery and financial invariants explicitly when real external effects are introduced.

## What this does not prove
It does not select Saga, Outbox, a message broker, an event store, a database transaction boundary, a payment provider protocol, or a service boundary.

## Evidence classification
- Prepared executable evidence: `ReversibilitySemanticsTest`.
- Inferred from code: facts are append-only in this test representation.
- Runtime-observed: only after the verification script is actually executed.
- Guaranteed production behavior: none; this is deliberately a semantic learning model.
