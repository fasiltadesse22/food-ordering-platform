# C2.1.2-P03 — Invariant → Required State → Ownership Pressure

## Engineering question
For each inherited invariant, which exact authoritative facts must be known and controlled at the point the invariant must hold, who can currently mutate those facts, and which mutations conflict?

## Dependency
P01 established identity continuity. P02 established value semantics and local validity. Neither answers which facts must be reasoned about together for a cross-state business invariant.

## Reasoning chain
invariant → required facts → current authority → mutation capability → conflicting mutations → forbidden state → atomicity/consistency pressure.

P03 stops before candidate consistency structures and transaction mechanisms.

## Terminology
**Required fact** is a fact that must be sufficiently current/correct to decide or preserve an invariant.

**Current authority** describes where the learning model currently treats a fact as authoritative. It is not a final architecture owner or deployment.

**Mutation capability** is a path able to change a required fact.

**Conflict** is two individually plausible decisions/mutations whose interleaving can violate the invariant.

**Forbidden state** is an authoritative combination the business rule says must not exist.

**Ownership pressure** means some authority must eventually control the correctness-relevant facts/effects strongly enough. P03 does not decide its final structure.

## Dependency matrix

| Invariant | Required facts | Current representation/authority | Conflict | Forbidden state |
|---|---|---|---|---|
| INV-ORDER-01 | payment consequence/lifecycle; current commercial terms; proposed terms | lifecycle/payment learning state + Order + incoming intent | establish payment vs replace terms | consequential paid agreement silently rewritten |
| INV-PAYMENT-01 | logical payment identity; existing successful effect; proposed effect | payment semantics + CorrectnessModel + attempt/result | two successes from same prior "none" observation | >1 successful effect for one logical payment |
| INV-ORDER-02 | Order identity; current authoritative lifecycle; proposed outcome | OrderId + lifecycle authority + incoming decision | accept vs cancel from same prior state | both exclusive outcomes authoritative |
| INV-REFUND-01 | payment identity; eligible captured value; cumulative refunds; proposed refund | specification only | independent refund approvals | cumulative successful refunds > eligible captured value |

## Facts are not automatically objects
Several required facts may be represented by several Java objects yet later be controlled together. Conversely, putting fields in one object does not provide atomicity, freshness, durability or concurrency safety.

number of fields/classes != number of consistency boundaries.

## Read dependency versus mutation authority
A decision can read a fact without controlling its mutation. Then:
1. decision maker observes X;
2. another authority changes X;
3. the first decision becomes authoritative using stale X.

This is the causal window already exposed by the inherited consistency-scope experiment.

## Atomicity pressure
Atomicity pressure means the business forbids a partial/intermediate authoritative result across related effects. It is a requirement statement, not yet "use a database transaction."

## Consistency pressure
Consistency pressure asks how current and mutually coherent required facts must be at the decision/commit point. Later design may answer with a local boundary, conflict detection, workflow redesign, reservation, conditional write or another justified mechanism.

## Evidence boundary
The catalog makes dependency reasoning executable. It proves no persistence, isolation, durability, concurrency control or distributed guarantee.
