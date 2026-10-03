# C2.1.2-P04 Checkpoint

Part 1.2.4 is **Deriving Candidate Aggregates from Invariants**.

## Scope
Use inherited invariant, required-fact, mutation-conflict and forbidden-state evidence to derive and compare candidate consistency boundaries. Do not infer Aggregates from nouns, tables, object graphs, bounded contexts, future services or deployments.

## Inherited baseline
C2.1.2-P03 remains the immediate baseline. Identity/value semantics and all Cluster 1.1/P03 correctness evidence remain authoritative.

## Added
- CandidateAggregateDesign architectural learning model;
- CandidateAggregateCatalog with four competing boundary hypotheses;
- separate Order/Payment and combined Order+Payment plausible candidates;
- deliberately undersized fragmented-Order candidate;
- deliberately oversized whole-ordering-graph candidate;
- executable comparison preserving the Order/Payment cross-boundary pressure;
- evidence artifact distinguishing invariant localization from coupling/contention risk.

## Current architectural conclusion
Known Order lifecycle/commercial correctness creates strong pressure for an Order-local consistency unit. Logical-payment successful-effect and refund accounting create strong pressure for a Payment-local consistency unit. The inherited Order-to-Payment acceptance dependency remains the principal unresolved boundary pressure.

Separate Order/Payment and combined Order+Payment are both examined because each resolves one force while worsening another. P04 does not declare a final production Aggregate merely from this comparison.

## Verification status
Prepared but not runtime-observed in this environment. scripts/verify.sh registers CandidateAggregateDerivationTest. Repository publication is not runtime PASS evidence.

## Deliberately unresolved
- final Aggregate and Aggregate Root selection;
- external mutation entry points and encapsulation;
- internal versus cross-Aggregate references;
- exact consistency guarantees;
- persistence/repository/schema;
- local transaction implementation;
- optimistic/pessimistic concurrency;
- cross-boundary coordination;
- service decomposition and deployment;
- Saga, Kafka, Outbox, Redis or distributed locks.

## Next part
P05 derives Aggregate Root and mutation authority from the candidate boundaries and inherited invariant-bypass evidence. It must show why outside mutation should pass through a controlling root rather than merely adding a class named AggregateRoot.
