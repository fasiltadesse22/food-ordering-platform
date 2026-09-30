# Part 1.1.4 — Identity and Authoritative State

## Scope contract
Cluster 1.1 requires identity and authoritative state before lifecycle, transitions, invariants, conflicts, atomicity and consistency can be reasoned about. This part establishes only that foundation.

## Identity
`OrderId` answers which continuing business thing a command or observation concerns. Descriptive equality is not identity: two orders may have the same customer, restaurant and requested items while remaining distinct orders. Conversely, an order may change represented state without becoming a different order.

This is not yet the Cluster 1.2 treatment of entities, aggregates, aggregate roots, repositories or consistency boundaries.

## State representation
`OrderStateSnapshot` is a point-in-time representation of facts currently represented for one `OrderId`. A snapshot is not automatically current merely because it exists in memory.

The booleans are intentionally crude learning scaffolding. They expose dimensions of state without yet defining a legal lifecycle or a final production domain model.

## Authority
`InMemoryOrderStateAuthority` makes one narrow claim concrete: within this single-process lab, the value returned by `current(orderId)` is the state selected as current for that identity.

It does NOT establish production-grade authority. It is:
- transient;
- non-durable;
- non-concurrent;
- non-transactional;
- not replicated;
- not a repository abstraction;
- not safe evidence of behavior after restart.

## Authoritative versus observed state
A caller may retain an earlier snapshot while the authority advances. The earlier object remains a valid historical observation but is no longer the current authoritative representation. This creates the conceptual basis for stale-decision experiments later.

## Deliberately unresolved
This part does not define which combinations of state are legal. In particular, the model can represent cancellation and completion simultaneously. That fragile state is intentional evidence that state representation alone does not define lifecycle correctness.

Part 1.1.5 must introduce lifecycle/state-machine semantics and determine legal/illegal transitions and terminal states without prematurely solving later invariant/concurrency problems.
