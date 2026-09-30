# Authority and Staleness Experiment

## Question
Can a state representation be valid as an observation yet cease to be authoritative for a later decision?

## Hypothesis
Yes. If a caller retains an earlier immutable snapshot and the designated authority advances to a newer snapshot for the same `OrderId`, the retained snapshot becomes stale without changing the order's identity.

## Prediction
After establishing state S0, reading S0, replacing the authority with S1, and then comparing both observations:
- both snapshots have the same `OrderId`;
- S0 still contains its earlier values;
- `authority.current(id)` returns S1;
- therefore possession of a snapshot does not prove freshness or authority.

## Setup
Single JVM; `InMemoryOrderStateAuthority`; immutable `OrderStateSnapshot`; no threads, database, network or persistence.

## Controlled variable
Only the authority's current snapshot for one `OrderId` changes.

## Constants
Order identity, customer identity, restaurant identity and requested items remain fixed.

## Execution
`AuthoritativeOrderStateTest.detachedSnapshotIsNotAutomaticallyAuthoritative()`.

## Observation
The earlier snapshot reports restaurant acceptance as false after the authority has advanced to a snapshot where it is true.

## Evidence
Executable test plus source state in this Git commit.

## Interpretation
Identity continuity and state freshness are separate concerns. A decision based on a previously observed snapshot can be stale even when it refers to the correct business identity.

## Limitations
This experiment does not prove database isolation, optimistic concurrency, thread safety, distributed consistency, durability, cache coherence or correctness after restart. No lifecycle legality is modeled yet.

## Conclusion
A correct target identity is necessary but insufficient for a correct decision. The decision also needs an authoritative-enough view of relevant state under explicitly defined consistency assumptions.
