# C2.1.2-P04 — Candidate Aggregates Derived from Invariants

## Engineering question
Which Entities and Value Objects plausibly belong in one consistency unit because inherited invariants require their state and mutations to be controlled together?

## Scope discipline
An Aggregate candidate is a hypothesis about a consistency boundary. It is not automatically:
- a Java object graph;
- a table group;
- a repository;
- a bounded context;
- a service;
- a deployment;
- a database;
- a transaction implementation.

P04 compares alternatives. It does not yet finalize root mutation authority (P05), reference/lifecycle integrity (P06), consistency semantics (P07), or local transaction implementation (P08).

## Derivation chain
invariant → required facts → mutation conflicts → forbidden state → minimal correctness closure → candidate consistency grouping → boundary-crossing invariants → trade-offs → candidate status.

## Candidate A — Order and Payment remain separate
Order candidate contains Order identity, lifecycle and commercial terms. Payment candidate contains logical payment identity, successful-effect accounting and refund accounting.

This localizes INV-ORDER-02 and the financial invariants while preserving the inherited Order↔Payment acceptance rule as an explicit cross-boundary pressure. INV-ORDER-01 requires careful modeling of what "payment consequence" must be known inside Order.

Strength: preserves independent lifecycles and smaller consistency surfaces.
Risk: stale Order/Payment decisions remain possible unless later architecture addresses them.

## Candidate B — combine Order and Payment
A larger candidate places Order lifecycle/commercial terms and Payment financial state inside one conceptual consistency unit.

Strength: the currently known Order/Payment rules can be evaluated against one candidate state boundary.
Risk: Order and Payment have different mutation/lifecycle forces; payment may involve external effects; unrelated financial activity can enlarge contention and transaction scope. Local containment also cannot make an external payment provider atomic.

## Candidate C — fragment Order state
Treat lifecycle and commercial terms as independent units.

Strength: small state units.
Risk: this cuts through INV-ORDER-01 and weakens the single authoritative lifecycle reasoning behind INV-ORDER-02. It creates coordination pressure for ordinary Order correctness.

This is a deliberately undersized counterexample.

## Candidate D — whole ordering graph
Combine Customer, Order, Restaurant, Payment and refund-related state because they are all "related."

Strength: many facts are reachable locally.
Risk: relationship is not an invariant. Customer and Restaurant identities/lifecycles are not shown by current evidence to require atomic mutation with each Order. The candidate maximizes coupling and contention without an invariant-based reason.

This is a deliberately oversized counterexample.

## Current P04 conclusion
The evidence makes Candidate A and Candidate B worthy of comparison; C and D are useful falsification boundaries. P04 does not yet declare the final production Aggregate design. The strongest current pressure favors keeping Order-internal correctness together and Payment financial accounting together while treating Order↔Payment rules as the key unresolved boundary question.

That conclusion remains provisional until P05-P08 add mutation authority, references/lifecycle, consistency semantics and transaction evidence.
