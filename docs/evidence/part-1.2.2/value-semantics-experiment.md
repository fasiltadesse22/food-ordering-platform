# C2.1.2-P02 — Value Semantics Experiment

## Specified
Domain concepts defined entirely by value should compare and behave by semantic value, reject locally invalid construction, and be replaceable without independent identity.

## Predicted
1. Independently created equal Money/Quantity values compare equal.
2. Invalid Quantity/Money states fail at construction.
3. Replacing Quantity in an OrderLine leaves the original value unchanged.
4. Composite equality/hash behavior follows component values.
5. Currency changes Money meaning; mixed-currency addition fails.
6. Explicit rounding produces deterministic normalized Money.
7. Two OrderId objects containing the same UUID are equal values while remaining distinct Java objects.
8. Raw primitives can erase semantic distinctions that domain types preserve.

## Prepared experiment
`ValueSemanticsTest` executes all eight cases deterministically in-process and is registered in `scripts/verify.sh`.

## Failure exposed by primitive obsession
A raw decimal `2` used for both amount and quantity is structurally indistinguishable at the primitive level. Domain types make `Money(2 ETB)` and `Quantity(2)` incompatible concepts.

## Failure exposed by invalid construction
If invalid local values are allowed to circulate, every consumer must repeatedly remember validation rules. Construction-time validity reduces the invalid representable state space.

## Failure exposed by mutable values
If a shared value is mutated in place, owners can observe changes they did not authorize. Replacement semantics avoid that class of aliasing failure.

## Evidence boundary
This experiment demonstrates in-process Java value semantics only. It does not prove:
- database constraints or persistence round trips;
- JSON/schema compatibility;
- concurrency safety of containing Entities/Aggregates;
- transaction atomicity;
- distributed consistency;
- universal monetary scale/rounding correctness.

## Inferred architectural pressure
P01 + P02 now distinguish identity-bearing concepts from value-defined concepts. The next question is no longer “Entity or Value Object?” in isolation; it is which authoritative facts an invariant needs and who must own mutation of those facts.
