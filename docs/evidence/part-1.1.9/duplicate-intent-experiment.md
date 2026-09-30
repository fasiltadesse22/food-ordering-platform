# Part 1.1.9 — Duplicate Intent Experiment

## Question
Can distinct execution-attempt identities still represent the same logical business intent and therefore create an invalid duplicate effect?

## Hypothesis
Yes. Deduplicating only by attempt/request identity is insufficient when retries receive new attempt identities but represent the same logical payment.

## Prediction
- a naive attempt-based recorder accepts `attempt-1/payment-42` and `attempt-2/payment-42` and records two effects;
- the existing logical-payment guard accepts the first `payment-42` success and rejects a sequential second success;
- `payment-42` and `payment-43` remain distinct legitimate intents.

## Setup
Use a test-only naive recorder beside the inherited `CorrectnessModel`. No production idempotency mechanism is added.

## Controlled variable
Identity used to decide sameness: execution-attempt identity versus logical business identity.

## Constants
The intended business invariant remains `successfulEffects(logicalPayment) <= 1`.

## Expected evidence
Assertions in `DuplicateIntentSemanticsTest` expose the duplicate effect in the naive model and the sequential protection in the inherited logical-identity model.

## Interpretation
Correct duplicate handling begins with business-operation identity. A transport identifier can be useful for tracing but does not necessarily identify business equivalence.

## Limitations
This experiment does not test concurrent races, process restart, durable storage, external payment-provider ambiguity, network retry, message redelivery or exactly-once semantics. Those must not be inferred from a sequential in-memory experiment.

## Evidence classification
Prepared executable evidence only until run in Java 21. No runtime PASS is claimed by the GitHub connector.
