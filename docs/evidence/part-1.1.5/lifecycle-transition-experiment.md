# Part 1.1.5 evidence — lifecycle transition experiment

## Question
Can the project distinguish representable state from legal business progression?

## Hypothesis
An explicit transition policy will allow known workflow progressions while rejecting illegal or terminal-state transitions.

## Prediction
The happy path reaches COMPLETED; cancellation before preparation reaches CANCELLED; cancellation from PREPARING is rejected; terminal states reject further ordering transitions; replaying an earlier transition from a later state is rejected.

## Setup
Single JVM, deterministic `OrderLifecycle`, no persistence, no network, no concurrency.

## Observation
`OrderLifecycleTest` exercises six lifecycle claims and passes.

## Evidence classification
- Observed: the executable transition policy accepts/rejects the tested transitions as documented.
- Inferred: making transition legality explicit prevents some sequential illegal state progressions that the Part 1.1.4 boolean snapshot could represent.
- Assumed: the documented cancellation cutoff and payment-before-restaurant ordering reflect the current learning-domain policy.
- Guaranteed: only the sequential transition behavior encoded and tested here.

## Limitations
This experiment does not prove atomicity, race safety, persistence durability, retry safety, refund correctness, or distributed consistency. Those pressures remain deliberately unresolved.
