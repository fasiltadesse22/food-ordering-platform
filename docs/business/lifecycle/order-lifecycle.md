# Order lifecycle — Part 1.1.5

## Scope

This model makes lifecycle legality explicit without claiming persistence, concurrency safety, aggregate boundaries, or distributed coordination.

## Current business assumptions

These assumptions are intentionally visible and revisable:

1. An order begins in `CREATED`.
2. Payment effect is established before restaurant acceptance is requested.
3. A restaurant can accept or reject only after payment is established.
4. Cancellation is allowed before preparation starts, including after restaurant acceptance.
5. Once preparation starts, cancellation is not a legal ordering-lifecycle transition in this lab.
6. `COMPLETED`, `CANCELLED`, and `RESTAURANT_REJECTED` are terminal **for the ordering lifecycle**.
7. Terminal ordering state does not imply all financial work is complete. A paid order that is rejected/cancelled may create refund work.
8. Moving "back" to an earlier state is not modeled as rollback. Compensation/refund is a new business action with its own facts.

## Transition matrix

| Current state | Establish payment | Restaurant accept | Restaurant reject | Start preparation | Complete | Cancel |
|---|---|---|---|---|---|---|
| CREATED | PAYMENT_ESTABLISHED | illegal | illegal | illegal | illegal | CANCELLED |
| PAYMENT_ESTABLISHED | illegal | RESTAURANT_ACCEPTED | RESTAURANT_REJECTED | illegal | illegal | CANCELLED |
| RESTAURANT_ACCEPTED | illegal | illegal | illegal | PREPARING | illegal | CANCELLED |
| PREPARING | illegal | illegal | illegal | illegal | COMPLETED | illegal |
| COMPLETED | illegal | illegal | illegal | illegal | illegal | illegal |
| CANCELLED | illegal | illegal | illegal | illegal | illegal | illegal |
| RESTAURANT_REJECTED | illegal | illegal | illegal | illegal | illegal | illegal |

## Important non-guarantees

The matrix defines sequential business legality. It does not make check-and-transition atomic. It does not prevent two callers from both deciding against the same earlier state. It does not persist state. It does not define an aggregate or service boundary. It does not make refund atomic with cancellation/rejection.
