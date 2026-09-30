# Use Cases — Part 1.1.2

These are business interactions, not endpoint or service definitions.

| Use case | Primary actor | Goal | Important alternatives / questions |
|---|---|---|---|
| Express ordering intent | Customer | Begin an attempt to obtain selected food | invalid/empty intent; restaurant unavailable; when does intent become authoritative order state? |
| Participate in payment | Customer / payment participant | Satisfy required financial condition | authorization vs capture; decline; unknown outcome; sequencing relative to restaurant decision |
| Accept requested fulfillment | Restaurant | Commit to fulfill | cancellation may already be requested; payment state may matter |
| Reject requested fulfillment | Restaurant | Refuse an order it cannot/will not fulfill | financial effect may already exist; final customer outcome required |
| Request cancellation | Customer | Stop further fulfillment where permitted | legality depends on lifecycle; may race with acceptance/preparation |
| Reverse/release financial effect | Payment participant | Correct a prior financial effect when business outcome requires it | refund vs void/release; failure/ambiguity deferred |
| Start preparation | Restaurant | Begin fulfillment work for an accepted order | whether cancellation remains legal changes here is unresolved |
| Complete fulfillment | Restaurant / platform | Establish completion according to product definition | preparation complete vs delivered/fulfilled is unresolved |

## Explicit deferral
Part 1.1.2 discovers use cases and workflows. It does not yet define command/event contracts, aggregate boundaries, a formal state machine, or concurrency-control mechanisms.
