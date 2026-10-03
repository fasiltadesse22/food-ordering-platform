# C2.1.2-P01 — Identity Continuity and Entity Semantics

## Engineering question
Which business concepts require identity that survives changes in their attributes and represented state?

## Inherited evidence
Cluster 1.1 established that `OrderId` identifies one continuing business thing and that an earlier `OrderStateSnapshot` can become stale while still referring to that same identity. It also distinguished logical payment identity from request/attempt identity. This part converts that evidence into explicit Entity reasoning without choosing Aggregate boundaries.

## Identity is not description
Two orders can have the same customer, restaurant and requested items and still be different Orders. Conversely, one Order can change represented state or descriptive values without becoming a new Order.

Therefore:

`same description != same entity`

and

`changed description != changed identity`

## Identity value versus Entity
`OrderId` is a typed value representing identity. `Order` is the continuing business Entity identified by that value.

This distinction prevents a common modeling error:

`has an ID != is an Entity`

A request ID, correlation ID, event ID, idempotency key or database row key can identify a technical occurrence without establishing a continuing domain Entity.

## Current classification

| Concept | Current identity reasoning | Classification established here? |
|---|---|---|
| Order | Continues through business state change under one OrderId | Entity |
| OrderId | Value representing Order identity | Identity value; Value Object treatment is P02 |
| Customer | CustomerId exists, but this project has not modeled enough Customer lifecycle/state to justify a concrete Customer Entity implementation here | Candidate identity-bearing domain concept; implementation deferred |
| Restaurant | RestaurantId exists, but Restaurant lifecycle/state is not modeled deeply enough here | Candidate identity-bearing domain concept; implementation deferred |
| logical payment | Cluster 1.1 proves logical identity differs from attempt/request identity | Identity distinction established; Payment Entity design deferred |
| payment attempt | A distinct occurrence may need its own identity, but lifecycle/model is not yet sufficient for final classification | Deferred |

## Equality semantics in the learning model
The C2.1.2-P01 `Order` learning model compares Orders by `OrderId`. Two different Java objects can therefore represent the same business Entity.

This is a domain-semantic experiment, not a claim that every production ORM/proxy implementation should copy this exact `equals/hashCode` strategy.

## Important preservation of Cluster 1.1
`Order.replaceRequestedItems` intentionally demonstrates identity continuity. It does **not** revoke INV-ORDER-01. A paid commercial agreement still cannot be silently rewritten.

The method remains a fragile mutation path because Aggregate Root authority and invariant-enforcement boundaries have not yet been derived. Removing that fragility here would prematurely solve P03-P05.

## Non-decisions
This part does not choose:
- Aggregate or Aggregate Root boundaries;
- repository boundaries;
- persistence mappings or database keys;
- transaction boundaries;
- optimistic/pessimistic concurrency;
- service boundaries;
- Payment aggregate design;
- durable identity generation strategy.

## Evidence claim
The executable test establishes only the modeled semantics:
1. equal descriptions do not collapse different Order identities;
2. descriptive change does not replace Order identity;
3. separate Java objects can represent the same business identity;
4. an OrderId value is not itself the Order Entity.

It does not prove persistence, concurrency, durability or distributed identity behavior.
