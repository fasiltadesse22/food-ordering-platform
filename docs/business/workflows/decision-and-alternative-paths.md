# Decision Points and Alternative Paths — Part 1.1.2

| Decision point | What must be decided | Currently known? | Why it matters later |
|---|---|---:|---|
| Admit ordering intent | Is the submitted intent eligible to enter the workflow? | Partial | defines transition from request/draft-like intent to authoritative order state |
| Payment sequencing | Financial step before or after restaurant acceptance? | No | changes customer latency, restaurant commitment, reversal frequency, failure windows |
| Restaurant decision | Can/will the restaurant fulfill? | Yes, decision exists; rules incomplete | establishes acceptance/rejection branch |
| Cancellation legality | Is cancellation still permitted now? | No | requires lifecycle/invariants and conflict handling |
| Financial reversal | What must happen after rejection/cancellation following financial effect? | No | requires precise payment semantics and failure handling |
| Start preparation | What prerequisites must hold? | No | likely irreversible-cost boundary and cancellation pressure |
| Completion | What business fact counts as complete? | No | affects lifecycle, user promises, downstream behavior |

## Alternative-path discovery questions
- payment declined before restaurant decision;
- restaurant rejects after financial effect;
- cancellation requested before acceptance;
- cancellation requested after acceptance;
- cancellation requested after preparation begins;
- acceptance and cancellation overlap;
- financial outcome is unknown rather than simply failed;
- preparation cannot be completed after acceptance.

The existence of a path is recorded here even when its correct outcome is not yet known. Unknown policy must not be converted into accidental code behavior.
