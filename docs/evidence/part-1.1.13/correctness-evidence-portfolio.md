# C2.1.13 — Correctness Evidence Portfolio

## Purpose
Part 1.1.13 closes Cluster 1.1 by converting the accumulated workflow, lifecycle, invariant, and failure experiments into a persistent evidence portfolio. This is a Type C evolution: the architecture is not expanded with a new production mechanism.

## Evidence discipline
Every claim is classified as one of:
- **Specified** — a business rule or assumption explicitly modeled by the project.
- **Prepared evidence** — executable source/test exists but runtime execution is not established merely by repository publication.
- **Observed** — reserved for results actually produced by executing the verification suite.
- **Inferred** — architectural conclusion supported by the model/experiment but not itself directly measured.
- **Not proven** — a guarantee deliberately outside the current evidence boundary.

## Portfolio

| Correctness concern | Business claim | Evidence artifact | What the artifact establishes | What it does not establish |
|---|---|---|---|---|
| Lifecycle legality | Order progress must follow explicitly legal transitions | `OrderLifecycleTest`, lifecycle transition matrix | Legal/illegal sequential transitions are executable and inspectable | Persistence, concurrency safety, transaction boundaries |
| Illegal transitions | Terminal/preparation states reject forbidden moves | `OrderLifecycleTest` | The current state machine rejects modeled illegal transitions | That every future mutation path uses this state machine |
| Duplicate payment | One logical payment must not produce more than one successful effect | `DuplicateIntentSemanticsTest`, INV-PAYMENT-01 | Attempt identity and logical business identity differ; sequential duplicate effect is rejected through the guarded model | Durable idempotency, external-provider atomicity, concurrent/crash-safe deduplication |
| Paid-order modification | Paid commercial terms cannot be silently rewritten | `ExecutableCorrectnessModelTest`, `InvariantBypassExperimentTest`, INV-ORDER-01 | Guarded mutation rejects the change; bypass experiment shows a guard alone is not global enforcement | Aggregate/transaction boundary or complete prevention of alternate mutation paths |
| Cancellation/acceptance race | Decisions valid against old state may conflict with newer authoritative truth | `StaleDecisionExperimentTest` plus lifecycle rules | A delayed acceptance can become stale after cancellation and revalidation detects the changed precondition | Atomic compare-and-set, locking, isolation, production race prevention |
| Conflicting restaurant outcomes | Accept and reject may each be legal from the same snapshot but cannot both be authoritative | `ConflictingConcurrentOperationsTest`, INV-ORDER-02 | Read/decide/write separation exposes a stale overwrite/conflicting-decision window | A chosen concurrency-control mechanism |
| Payment-after-cancellation / cross-fact staleness | Local order legality is insufficient when acceptance depends on independently mutable payment truth | `ConsistencyScopePressureTest` | Order-side legality can remain true while combined order/payment validity becomes false | Service boundaries, distributed transactions, Saga, aggregate design |
| Refund correctness | Refund must not exceed eligible captured value | INV-REFUND-01 | The business invariant is explicitly recorded | Executable refund subsystem, partial-refund policy, atomic accounting enforcement |

## Evidence chain
The portfolio should be read as one causal chain:

`workflow -> authoritative facts -> lifecycle -> invariants -> failure window -> controlled experiment -> evidence -> architectural pressure`

No individual passing test proves system-wide correctness. The evidence is intentionally bounded by the model, mutation path, execution mode, and failure window tested.

## Cluster-level conclusion
Cluster 1.1 has discovered **what must remain correct** before choosing **where correctness will be enforced**. The next cluster may use this evidence to derive entity/value-object/aggregate ownership and local consistency boundaries without inventing them from database tables or service preferences.
