# C2.1.7 — Invariants: The Truths Architecture Must Protect

Part 1.1.7 treats invariants as business specifications that architecture must preserve. It deliberately separates the truth itself from validation, database constraints, and other enforcement mechanisms.

## INV-ORDER-01 — Paid commercial agreement is not silently rewritten
Once payment success makes commercial terms consequential, quantity/price/restaurant/order-line terms cannot be mutated as if payment had never happened. A later change requires an explicit business workflow whose semantics are separately defined.

- Scope: one logical order and the commercial agreement referenced by payment.
- Violation example: `PAID` order quantity changes from 2 to 5 with no amendment/refund/re-price workflow.
- Not equivalent to validation: `quantity > 0` can be true while this invariant is false.
- Enforcement status in C2.1.7: one explicit guard exists, but bypass paths are intentionally demonstrated; global enforcement is not claimed.

## INV-PAYMENT-01 — At most one successful effect per logical payment
For logical payment identity `p`, `successfulEffects(p) <= 1`.

- Request identity is not logical-payment identity.
- Multiple attempts may be legitimate; multiple successful business effects for the same logical payment are not.
- A future database constraint may help enforce this, but a constraint is not the invariant and must match the exact business cardinality.
- Enforcement status in C2.1.7: sequential in-memory check only; concurrency, crash safety, durability, and external-provider effects remain unproven.

## INV-ORDER-02 — Mutually exclusive final outcomes cannot both be authoritative
Where cancellation and restaurant acceptance are semantically exclusive for the same lifecycle point, both cannot become authoritative outcomes for one order.

- Sequential lifecycle guards demonstrate one protected path.
- Concurrent decisions from the same prior state remain a future experiment.

## INV-REFUND-01 — Refund cannot exceed eligible captured value
For one payment, cumulative successful refund value must not exceed the amount that is eligible to be refunded under the business policy.

This is recorded now as a business invariant, not implemented as a payment/refund subsystem. Exact partial-refund policy remains a domain question and must be clarified before implementation.

## Specification vs enforcement

`invariant -> required truth`

`guard / type / transaction / constraint / lock / conditional write / idempotency record -> possible enforcement mechanisms`

A passing test through one guarded API proves that path behaved as expected under the tested conditions. It does not prove that every mutation path, concurrent execution, crash window, restart, or future service boundary preserves the invariant.

## Atomicity requirement register

| Invariant | Competing/related operations | State that must be reasoned about together | Current guarantee | Future pressure |
|---|---|---|---|---|
| INV-ORDER-01 | pay vs commercial-term modification | lifecycle/payment consequence + commercial terms | guarded sequential example only | derive ownership/transaction boundary |
| INV-PAYMENT-01 | duplicate/retried successful payment | logical payment identity + successful effect record | sequential check-then-act only | atomic check/effect/recording problem |
| INV-ORDER-02 | cancel vs accept | authoritative order lifecycle decision | sequential transition protection only | concurrent conflict handling |
| INV-REFUND-01 | multiple refunds | captured/eligible amount + cumulative successful refunds | specification only | atomic financial accounting boundary |

The register states correctness pressure. It intentionally does not choose aggregates, database transactions, optimistic locking, distributed locks, Kafka, Saga, Outbox, or any other later mechanism.
