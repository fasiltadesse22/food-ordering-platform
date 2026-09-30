# C2.1.11 — Stale Decisions

## Purpose
Isolate a deeper correctness problem exposed by C2.1.10: a decision can be valid when it is made and invalid by the time its effect is applied because the truth on which it depended has changed.

## Core model

`observe truth -> derive decision -> time passes -> truth changes -> apply old decision`

The defect is not simply "two threads ran at once." The important semantic failure is that a decision carries assumptions from an earlier state into a later authoritative state without proving that those assumptions still hold.

## Decision validity has a time dimension
A decision is justified by a set of facts and preconditions. If any correctness-relevant fact changes before the decision becomes authoritative, the decision may be stale.

For an order initially in `PAYMENT_ESTABLISHED`:
1. restaurant path observes that acceptance is legal;
2. it pauses before applying the acceptance;
3. another operation changes the authoritative order to `CANCELLED`;
4. the restaurant path resumes using its old decision;
5. blindly applying `RESTAURANT_ACCEPTED` would make a decision from obsolete truth authoritative.

## Stale read vs stale decision
A stale read is old information. A stale decision is an action/choice whose justification depends on information that is no longer authoritative. A stale read is not automatically harmful; harm occurs when obsolete information influences an effect that must respect current truth.

## Correctness question
The important question is not "did I read a valid state?" It is:

> Are the assumptions that justified this decision still true at the point where its effect becomes authoritative?

## Current architectural pressure
The project deliberately does not solve stale decisions yet. This part establishes the force that later motivates mechanisms such as revalidation, serialization, compare-and-set/version checks, optimistic concurrency, pessimistic locking, transaction isolation, or workflow redesign.

Those mechanisms are alternatives, not synonyms, and none is introduced merely because stale decisions exist.

## Guarantees and non-guarantees
The experiment demonstrates that:
- a decision can be historically valid yet currently invalid;
- reevaluating against current authoritative state can detect the changed precondition in the modeled lifecycle;
- blindly applying the old decision can produce an outcome the current lifecycle would reject.

It does not demonstrate or guarantee:
- production-safe concurrency control;
- database isolation;
- optimistic or pessimistic locking;
- atomic compare-and-set;
- distributed coordination;
- durable state or restart recovery.

## Design questions carried forward
- Which facts make a decision valid?
- How long may that decision remain valid?
- Which changes invalidate it?
- At what boundary must validity be checked?
- Must observation and effect be atomic, or is conflict detection sufficient?
- Can the workflow be redesigned so stale decisions are harmless or compensatable?
