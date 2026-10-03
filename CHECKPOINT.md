# C2.1.2-P01 Checkpoint

Part 1.2.1 is **Identity Continuity and Entity Semantics**.

## Inherited baseline
C2.1.13 remains the authoritative Cluster 1.1 handoff. Its lifecycle, invariant and failure-window evidence is preserved.

## Added
- explicit `Order` Entity learning model;
- executable identity-continuity experiment;
- distinction between Entity, typed identity value, Java object identity and descriptive equality;
- persistent learning/evidence artifacts for P01.

## Observed by repository inspection
The source and verification registration are present on branch `course2/c2-1-2-p01-entity-identity`.

## Runtime verification status
Prepared but not observed in this environment. The repository's `scripts/verify.sh` compiles all Java 21 main/test sources and now registers `EntityIdentitySemanticsTest`, but repository publication alone is not runtime PASS evidence.

## Deliberately preserved fragile state
`Order.replaceRequestedItems` demonstrates identity continuity but does not yet globally enforce INV-ORDER-01. Aggregate Root authority and invariant ownership belong to later parts.

## Deliberately unresolved
- Value Object treatment beyond existing typed identity values;
- invariant-to-required-state ownership analysis;
- Aggregate/Aggregate Root boundaries;
- repositories/persistence;
- transaction boundaries;
- optimistic/pessimistic concurrency;
- service decomposition and distributed consistency.

## Next part
P02 derives Value Object/value semantics without using tables, ORM mappings or aggregate conventions as the starting point.
