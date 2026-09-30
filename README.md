# Food Ordering Platform — Course 2 / V7.1

This repository is the continuously evolving Food Ordering Platform for the Enterprise Distributed Systems Architecture & System Design course.

## Current learning checkpoint
**Cluster 1.1 / Part 1.1.4 — Identity and Authoritative State**

This checkpoint inherits Part 1.1.3 and makes identity, state representation, authority, and stale observation executable. It deliberately does not yet define lifecycle legality, invariants, aggregate boundaries, persistence, concurrency control, or distributed consistency.

The state authority is an educational single-process mechanism, not a production repository or durability guarantee.

## Verify
Run:

```bash
./scripts/verify.sh
```

The script compiles Java 21 sources and runs the dependency-free executable tests. `pom.xml` remains available for Maven-shaped project evolution when Maven is available.

## Learning evidence
- `docs/business/workflows/`
- `docs/evidence/part-1.1.2/`
- executable scenarios in `OrderingWorkflowCatalog`
- tests preserving unresolved policy rather than silently guessing it
