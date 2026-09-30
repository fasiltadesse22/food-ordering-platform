# Commands, Decisions and Facts — Part 1.1.3

## Purpose
Make the semantic boundary explicit before lifecycle, persistence, messaging, or distributed infrastructure is introduced.

## Command / intent
A command expresses a desired business change. `RequestCancellation(orderId)` means the customer asks for cancellation. It does not prove that cancellation is legal, accepted, persisted, or completed.

## Decision
A decision evaluates an intent against currently authoritative rules/state. This checkpoint distinguishes accepted, business-rejected, and unresolved decisions. `Unresolved` is deliberate: Chapter B has not yet established authoritative lifecycle state or cancellation guards.

## Fact
A fact describes an outcome this learning model treats as established. A fact is not automatically a Kafka message, integration event, database row, or proof of durable persistence.

## Core distinction
`RequestCancellation` != `OrderCancellationAccepted`.
`RequestRestaurantAcceptance` != `RestaurantFulfillmentAccepted`.

## Business failure vs technical failure
A restaurant deciding it cannot fulfill is a valid business rejection. A Java exception, timeout, unavailable database, or network failure would be a technical failure. This checkpoint models business rejection only; it does not yet model infrastructure failures.

## Explicit deferrals
- authoritative Order state and lifecycle
- guards and legal/illegal transitions
- durable persistence/transactions
- command identity/idempotency
- messaging/domain-event publication
- concurrency and stale-state protection
