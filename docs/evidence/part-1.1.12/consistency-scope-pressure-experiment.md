# Part 1.1.12 — Consistency Scope Pressure Experiment

## Question
What changes when a business decision depends on more than one independently mutable fact?

## Hypothesis
A decision that is valid against one component's current state can still violate the combined business invariant if another correctness-relevant component changes after observation and before the effect becomes authoritative.

## Model
The experiment intentionally keeps both components in memory. Distribution is not required to create the semantic problem.

Facts:
- order lifecycle state;
- payment fact.

Combined acceptance invariant for the experiment:

`restaurant acceptance may become authoritative only while order acceptance is legal AND payment remains established`

## Controlled timeline
T0 order = PAYMENT_ESTABLISHED, payment = ESTABLISHED
T1 acceptance observes both facts
T2 combined acceptance decision is valid
T3 payment changes independently to REFUNDED
T4 order state itself still permits ACCEPT_BY_RESTAURANT
T5 order-only validation therefore says ACCEPT is legal
T6 combined re-evaluation rejects acceptance because payment is no longer established
T7 blindly applying the old combined decision would produce RESTAURANT_ACCEPTED with payment REFUNDED

## Prediction
The experiment should demonstrate that:
1. local order legality can remain true;
2. the cross-component business invariant can simultaneously be false;
3. therefore correctness cannot be inferred from one component's state alone when the invariant depends on multiple facts.

## Evidence
`ConsistencyScopePressureTest` deterministically models the timeline and asserts the divergence between local order legality and combined business validity.

## Interpretation
The important conclusion is not "use a distributed transaction." The conclusion is that invariant scope creates consistency-boundary pressure. We must first discover which facts belong to one correctness decision before deciding how state should be grouped or coordinated.

## Limitations
This experiment does not prove:
- that Order and Payment should be separate aggregates or services;
- that they should be stored in different databases;
- that ACID cannot solve the eventual implementation;
- that Saga, messaging, locking, or distributed transactions are required;
- any production concurrency guarantee.

## Evidence classification
The source is prepared executable evidence. Runtime PASS is established only by executing the verification suite in a Java 21 environment.
