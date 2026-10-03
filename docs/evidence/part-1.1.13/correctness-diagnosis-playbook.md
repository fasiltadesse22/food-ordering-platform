# C2.1.13 — Correctness Diagnosis Playbook

## Purpose
Turn the Cluster 1.1 models into a repeatable production-style diagnostic method.

## Diagnostic sequence
1. **State the symptom without mechanism language.** Example: an order appears accepted although the correctness-relevant payment fact no longer supports acceptance.
2. **Name the violated invariant.** If no invariant can be stated, the investigation is not yet grounded.
3. **Identify authoritative facts.** Separate authoritative state from projections, cached views, historical observations, and derived decisions.
4. **Reconstruct the business timeline.** Record observation, decision, competing mutation, authoritative effect, and later observation.
5. **Identify logical operation identity.** Distinguish retries/deliveries from genuinely distinct business intent.
6. **Locate the failure window.** Examples: check→act, observe→decide→apply, effect→record, read fact A→fact B changes→apply decision.
7. **Classify the failure.** Illegal transition, invariant bypass, duplicate effect, conflicting decisions, stale decision, or cross-fact consistency failure.
8. **Separate evidence from hypothesis.** Logs/tests/state snapshots are evidence; “a race probably happened” is a hypothesis until reconstructed.
9. **State the missing guarantee.** Examples: one authoritative mutation path, atomic check/effect, stale-decision detection, bounded duplicate effect.
10. **Only then evaluate mechanisms.** Cluster 1.1 stops before choosing aggregate, transaction, locking, messaging, or distributed-coordination mechanisms.

## Failure reconstruction template
```
Invariant:
Logical operation(s):
Authoritative facts:
Initial truth:
Observation(s):
Decision(s):
Competing mutation:
Authoritative effect:
Forbidden/incorrect outcome:
Failure window:
Evidence:
Assumptions:
Missing guarantee:
Mechanism deliberately not yet selected:
```

## Example — delayed acceptance after cancellation
- Invariant pressure: an obsolete acceptance decision must not override a later authoritative cancellation.
- Initial truth: order is PAYMENT_ESTABLISHED.
- Observation: restaurant path sees acceptance as legal.
- Competing mutation: cancellation changes authoritative order state to CANCELLED.
- Failure window: decision is retained between observation and application.
- Incorrect outcome: old acceptance result is blindly applied.
- Evidence: `StaleDecisionExperimentTest`.
- Missing guarantee: validity of the decision must somehow be preserved, re-established, or conflict-detected at the authoritative effect boundary.
- Not concluded here: optimistic locking, pessimistic locking, transaction isolation, or distributed coordination.

## Staff-level diagnostic standard
A diagnosis is incomplete if it jumps from symptom directly to technology. The expected chain is:

`symptom -> violated invariant -> authoritative truth -> timeline -> failure window -> missing guarantee -> candidate mechanisms/trade-offs later`.
