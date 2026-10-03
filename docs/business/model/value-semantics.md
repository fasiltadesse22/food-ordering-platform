# C2.1.2-P02 — Value Semantics and Domain Values

## Engineering question
Which domain concepts are defined entirely by their value rather than by continuing identity?

## Dependency on P01
P01 established the Entity side of the distinction: Order has continuity under OrderId even as descriptive state changes. P02 establishes the complementary model: a Value Object has no independent continuing identity; its meaning is determined by its constituent values.

## Derived domain values introduced
- Quantity: positive count with domain meaning distinct from an arbitrary integer.
- Money: amount + currency + explicit precision/rounding policy.
- OrderLine: a composite value in this learning checkpoint, defined by item name, quantity and unit price.
- Existing OrderId/CustomerId/RestaurantId: typed identity values whose own equality is value-based even though they identify Entities/domain concepts.

These classifications are deliberately local to the evidence available at P02. OrderLine is not declared an Entity merely because a future persistence model might give a row an ID.

## Value semantics
For a Value Object V, two independently allocated instances with the same normalized constituent values are interchangeable for the modeled purpose.

Value equality is therefore structural/semantic rather than based on Java reference identity.

## Replace, do not mutate
Value Objects are immutable. A change produces a replacement value. This avoids temporal aliasing such as one holder unexpectedly observing another holder's in-place mutation.

## Validity by construction
Quantity rejects zero/negative values. Money rejects negative amounts in this deliberately narrow learning model. These are local value-validity rules, not substitutes for cross-state business invariants such as INV-ORDER-01.

## Semantic types versus primitive obsession
A bare integer/decimal does not encode whether it means quantity, money, percentage, duration or another concept. Semantic types narrow the representable state space and make illegal substitutions harder.

## Money-specific semantics
Money is not merely BigDecimal:
- currency is part of the value;
- precision/scale and rounding policy must be explicit;
- arithmetic must preserve currency semantics;
- mixed-currency addition is rejected rather than silently interpreted.

P02 uses scale 2 and HALF_EVEN as explicit experiment policy. This is not a universal currency rule.

## Composite values and collections
OrderLine demonstrates a composite value. If collections become components of Value Objects, their equality, ordering/duplicate semantics and immutability become part of the value definition; this checkpoint does not invent such a collection merely to demonstrate syntax.

## Temporal values
Time-like concepts require explicit semantics: instant vs local date/time, timezone/offset, interval boundaries, and precision can change equality and validity. No temporal Value Object is added because P02 has no current Food Ordering requirement that justifies one.

## Serialization
A serialized representation must preserve semantic components needed to reconstruct the same value. Serialization shape is not itself the domain definition, and no persistence/API contract is chosen in P02.

## When a value becomes an Entity
If the business must track one particular occurrence through time, distinguish independent histories, or refer to it independently despite attribute changes, identity pressure has appeared. Reclassify from evidence rather than attaching an ID mechanically.

## Explicit non-goals
P02 does not choose:
- Aggregate/Aggregate Root boundaries;
- invariant ownership/mutation authority;
- persistence or ORM mapping;
- transaction boundaries;
- concurrency/versioning mechanisms;
- service boundaries;
- exchange-rate/conversion policy;
- tax/discount/pricing architecture;
- MenuItem identity.

The next part consumes these semantic types while tracing invariants to the exact authoritative state required to evaluate them.
