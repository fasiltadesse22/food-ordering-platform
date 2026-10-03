# C2.1.2-P04 — Candidate Aggregate Comparison Experiment

## Question
Can Aggregate candidates be derived and falsified from inherited invariant/fact/mutation evidence rather than from nouns, tables, object navigation or future service guesses?

## Hypothesis
A useful candidate boundary will localize a meaningful set of invariant closures without pulling in unrelated independent state; boundaries that cut through frequent correctness relationships create coordination pressure, while boundaries that absorb unrelated state create coupling/contention pressure.

## Prediction
- Fragmenting Order lifecycle from its correctness-relevant commercial state will cut through inherited Order invariants.
- Combining the whole ordering graph will include state unsupported by current invariant evidence.
- Separate Order/Payment and combined Order+Payment candidates will expose the central trade-off rather than eliminating it by assumption.

## Setup
CandidateAggregateCatalog records four competing hypotheses. CandidateAggregateDerivationTest verifies that alternatives remain explicit, that the separate candidate preserves the inherited cross-fact acceptance pressure, and that deliberately undersized/oversized designs are retained for falsification.

## Controlled variable
Only the proposed consistency grouping changes. No root, repository, database, transaction, lock, version, service or messaging mechanism is introduced.

## Expected observation
Candidate quality cannot be inferred from object reachability. The comparison should show:
- too small → invariant crosses boundary and requires extra coordination;
- too large → unrelated lifecycle/mutation state enters one consistency unit;
- plausible middle candidates → explicit correctness versus coupling trade-off.

## Evidence classification
- Specified: inherited four invariants and P03 dependency graphs.
- Predicted: C is undersized; D is oversized; A/B expose the main current design tension.
- Prepared: candidate catalog, comparison test and this artifact.
- Observed: not claimed until verification executes.
- Inferred: current evidence supports Order-local and Payment-local consistency as serious candidates but does not yet settle every cross-boundary rule.
- Not proven: final Aggregate/Root selection, persistence, transaction alignment, concurrency control, external-payment correctness, or distributed consistency.

## Limitation
This experiment compares architectural hypotheses using current evidence. Later requirements can change invariant closures and therefore change the preferred boundary.
