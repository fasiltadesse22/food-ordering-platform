# Part 1.1.11 — Stale Decisions Experiment

## Question
Can a business decision be correct when derived, become invalid before it is applied, and still escape if the system applies it without checking the truth on which it depended?

## Hypothesis
Yes. Decision validity is conditional on the facts used to derive the decision. If a correctness-relevant fact changes during the gap between decision and effect, blindly applying the old decision can violate the current lifecycle even though the original decision was legitimate.

## Prediction
Starting from `PAYMENT_ESTABLISHED`:
- acceptance is initially allowed;
- after a competing cancellation moves authoritative state to `CANCELLED`, a fresh acceptance decision is rejected;
- the previously computed acceptance decision still contains `RESTAURANT_ACCEPTED` as its intended result;
- blindly writing that old result would therefore bypass the current precondition;
- revalidation against current authoritative state detects the stale decision.

## Setup
Use the inherited `OrderLifecycle` unchanged. The experiment uses a tiny test-only mutable holder to separate decision time from effect time. No production locking, version field, transaction, repository, database, or compare-and-set is introduced.

## Controlled timeline

T0 authoritative state = PAYMENT_ESTABLISHED
T1 restaurant reads PAYMENT_ESTABLISHED
T2 restaurant decides ACCEPT -> RESTAURANT_ACCEPTED (valid at T2)
T3 restaurant pauses
T4 cancellation reevaluates current PAYMENT_ESTABLISHED and applies CANCEL -> CANCELLED
T5 restaurant resumes with its T2 decision
T6 fresh ACCEPT against CANCELLED -> REJECTED
T7 naive path applies old T2 result anyway -> RESTAURANT_ACCEPTED

## What is true vs what participants know
At T2, the restaurant's decision is justified by the truth it observed. At T4, global authoritative truth changes. The stored decision does not magically update; it contains a conclusion derived from an earlier world. At T5, possession of an earlier valid decision is not proof that its preconditions remain true.

## Controlled variable
Whether the delayed operation revalidates against current authoritative state before applying its effect.

## Constants
- same order lifecycle policy;
- same initial state;
- same intended restaurant acceptance;
- same competing cancellation;
- no concurrency-control mechanism.

## Expected evidence
`StaleDecisionExperimentTest` proves in the deterministic model that:
1. the original acceptance decision is allowed;
2. cancellation changes authoritative state;
3. fresh acceptance is rejected from the new state;
4. blindly applying the old decision can nevertheless overwrite the authoritative state;
5. revalidation prevents that modeled stale effect.

## Interpretation
The experiment separates *decision correctness at derivation time* from *decision validity at commit/effect time*. The architectural pressure is to establish a trustworthy relationship between the facts observed and the state to which the effect is applied.

## Limitations
This is a deterministic semantic experiment, not a real multithreaded or database concurrency test. It does not compare locking/versioning/isolation performance, prove atomicity, or solve distributed stale-state problems.

## Evidence classification
The Java source is executable evidence prepared in the repository. Runtime PASS must only be claimed after `scripts/verify.sh` is actually executed in a Java 21 environment.
