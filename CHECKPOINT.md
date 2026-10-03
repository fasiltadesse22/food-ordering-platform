# C2.1.2-P02 Checkpoint

Part 1.2.2 is **Value Semantics and Domain Values**.

## Scope contract
Engineering question: **Which domain concepts are defined entirely by their value rather than by continuing identity?**

This checkpoint follows the V7.1 Cluster 1.2 progression and the derived blueprint. It expands explanation/evidence without moving ahead into invariant-state ownership or Aggregate design.

## Inherited baseline
C2.1.2-P01 remains the immediate baseline. Order Entity identity continuity and all inherited Cluster 1.1 lifecycle/invariant/failure-window evidence are preserved.

## Added
- `Quantity` semantic Value Object with validity by construction;
- `Money` Value Object with amount, currency, explicit scale/rounding and currency-safe arithmetic;
- `OrderLine` composite Value Object with replace-not-mutate semantics;
- executable equality/hash, primitive-obsession, invalid-construction, currency and rounding experiments;
- explicit treatment of typed IDs as value-semantic identity values without confusing them with Entities;
- documentation of collection, temporal and serialization semantics without inventing unjustified production types.

## Runtime verification status
Prepared but not observed in this environment. `scripts/verify.sh` compiles all Java 21 sources and registers `ValueSemanticsTest`, but repository publication is not runtime PASS evidence.

## Deliberately preserved fragile/unresolved state
- `Order.replaceRequestedItems` still does not globally enforce INV-ORDER-01;
- Order still stores its earlier requested-item representation; P02 does not perform a whole-project redesign;
- no Aggregate/Aggregate Root has been declared;
- no invariant ownership/mutation authority has been chosen;
- no persistence, repository, transaction or versioning mechanism exists;
- no service/distributed boundary is introduced;
- no exchange-rate, tax, discount or broader pricing architecture is invented.

## Evidence claim
P02 establishes in-process value semantics: structural equality, semantic typing, local validity, immutability/replacement, composite values, currency-sensitive Money and explicit rounding behavior. It does not prove persistence, serialization compatibility, concurrency, transactions or distributed correctness.

## Next part
P03 traces selected inherited invariants to the exact authoritative state and mutation relationships required to evaluate/protect them. That analysis must precede Aggregate boundary selection.
