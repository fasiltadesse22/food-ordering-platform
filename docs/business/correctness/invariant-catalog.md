# C2.1.6 — Executable Correctness Model

This catalog turns selected business truths into explicit correctness claims and executable examples. It is deliberately still a single-process learning model: persistence transactions, optimistic locking, aggregates, Kafka, Saga, Outbox, and distributed coordination are deferred.

## Invariants

### INV-ORDER-01 — Paid-order commercial terms are immutable
Once an order is `PAID`, its commercial terms must not be silently changed. A change requires an explicit later business workflow rather than mutation of the already-paid agreement.

This is a business invariant, not merely input validation. A field-level check such as `quantity > 0` cannot protect it because legality depends on lifecycle history.

### INV-PAYMENT-01 — One successful payment effect per logical payment
For one logical payment identity, at most one successful payment effect may be recorded.

This is stronger than saying that two requests cannot share a request ID. Retries may use different transport/request identities while still representing the same logical payment.

### INV-ORDER-02 — Restaurant acceptance and cancellation cannot both become final outcomes
For one order, terminal business outcomes that semantically conflict must not both be established. The current executable model demonstrates the invariant with a single authoritative decision point; durable/concurrent enforcement is intentionally deferred.

## Validation rule versus invariant versus database constraint

- Validation rule: a proposed quantity must be greater than zero.
- Business invariant: a paid order cannot be silently modified.
- Database constraint: a future persistence mechanism might use `UNIQUE(logical_payment_id)` as one enforcement mechanism for INV-PAYMENT-01.

The database constraint would be an implementation mechanism, not the business truth itself.

## Atomicity implications

A check followed by a state change is safe only if competing operations cannot invalidate the checked condition before the change becomes authoritative. The current in-memory model provides a single method boundary for reasoning, but it does **not** claim database atomicity or thread safety.

Future parts/clusters must derive transaction and concurrency-control boundaries from these invariants rather than from table relationships.

## Consistency implications

If state later crosses process or ownership boundaries, each invariant must be classified by how fresh/authoritative the deciding state must be. A stale read may be acceptable for display while being unacceptable for a decision that protects INV-PAYMENT-01 or mutually exclusive terminal outcomes.
