# End-to-End Workflow Discovery — Part 1.1.2

## Why workflows, not endpoints
A use case is a business interaction toward an outcome. An endpoint is one possible interface mechanism. We therefore model the business path before choosing HTTP, messaging, service boundaries, or persistence ownership.

## W1 — Successful fulfillment (provisional)
Customer expresses ordering intent → ordering intent is admitted to workflow → required financial participation occurs → restaurant decides → restaurant accepts → preparation occurs → workflow reaches a completion outcome.

This path is provisional because payment sequencing and the exact meaning of completion remain unresolved.

## W2 — Restaurant rejects after a financial effect
Customer expresses ordering intent → financial effect occurs → restaurant decides it cannot/will not fulfill → rejection becomes the business outcome → platform must determine the required financial reversal/release and final order outcome.

Architectural pressure exposed: a multi-step workflow can create consequences that must be handled when a later business decision invalidates the happy path. This document deliberately does not label the solution a Saga.

## W3 — Cancellation requested before restaurant decision is known
Customer expresses ordering intent → financial participation occurs → customer requests cancellation → cancellation legality must be decided → restaurant decision may be concurrent/unknown → financial consequence must be determined.

Architectural pressure exposed: ordering of business actions and authority over conflicting decisions will matter. The concurrency solution is deliberately deferred until the invariant and atomicity work.

## W4 — Restaurant-first rejection alternative
Customer expresses ordering intent → restaurant rejects → no unnecessary financial effect should occur if product policy permits restaurant-first sequencing.

This is an alternative, not the chosen workflow. It exists to force an explicit comparison of sequencing trade-offs rather than silently assuming payment-first.
