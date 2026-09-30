# Part 1.1.13 — Coordination Cost Pressure Experiment

## Question
If correctness requires conflicting operations to agree on shared business truth, what changes when we serialize only the conflicting correctness scope instead of allowing blind interleaving?

## Hypothesis
A minimal coordination boundary can prevent the impossible combined outcome demonstrated in C2.1.12, but overlapping conflicting work loses some concurrency. Wider-than-necessary coordination increases that cost without strengthening the invariant.

## Model
This is a deterministic semantic experiment, not a production concurrency benchmark. It models three schedules:

1. **Uncoordinated conflicting schedule** — acceptance derives a decision, refund changes payment, then stale acceptance becomes authoritative. The combined invariant is violated.
2. **Minimal conflicting-operation coordination** — acceptance completes its correctness-relevant read/effect before refund enters the same coordination boundary. The impossible intermediate outcome is excluded by serialization of the conflict.
3. **Over-broad coordination** — an unrelated operation is forced through the same boundary, demonstrating unnecessary serialization pressure.

## Prediction
- Uncoordinated conflicting operations can reproduce `RESTAURANT_ACCEPTED + REFUNDED`.
- Serializing the conflicting acceptance/refund correctness scope prevents that specific stale-decision escape in the model.
- Serializing an unrelated operation adds coordination participation without contributing to preservation of the acceptance/payment invariant.

## Evidence
`CoordinationCostPressureTest` asserts all three semantic schedules and counts coordination-boundary participation to make the scope difference explicit.

## Interpretation
The experiment supports a design principle, not a technology choice:

`coordinate what correctness requires; do not coordinate unrelated work merely because it is convenient.`

## Limitations
This experiment does not measure lock latency, throughput, database contention, isolation levels, distributed availability, or production scheduling. It does not establish which concrete coordination mechanism should be selected. Those require later project states and controlled runtime experiments.

## Evidence classification
- **Observed after Java 21 execution:** deterministic assertions and PASS output.
- **Inferred:** wider coordination can create unnecessary serialization pressure.
- **Not established here:** quantitative production cost or the correct implementation mechanism.
