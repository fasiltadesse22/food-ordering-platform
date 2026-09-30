# C2.1.13 Checkpoint

Part 1.1.13 is **Consistency Implications and the Cost of Coordination**.

## Predecessor
C2.1.12 at `966853b077df888b5b92e71ea5f08ebf3bf5b26b`.

## Added
- derivation from invariant scope to required agreement and coordination scope;
- explicit distinction between correctness requirements and coordination mechanisms;
- semantic comparison of uncoordinated conflicting work, minimal conflict coordination, and intentionally over-broad coordination;
- evidence that coordination should be scoped to correctness-relevant conflicts rather than unrelated work;
- quality-attribute reasoning around concurrency, waiting, contention, availability coupling, failure surface, and operational complexity.

## Architectural conclusion
Coordination is a cost paid to preserve a required correctness property. The design objective is not "avoid coordination" and not "coordinate everything". It is to identify the smallest correctness-relevant scope, determine the guarantee actually required by the business invariant, and later select the least costly mechanism that provides that guarantee under the real workload and failure model.

## Deliberately unresolved
- aggregate and repository boundaries;
- concrete local ACID transaction design and isolation level;
- optimistic/pessimistic concurrency control;
- distributed transaction protocols;
- Saga/compensation design;
- service decomposition;
- Kafka, Outbox, Redis, distributed locks, or eventual-consistency mechanisms.

## Verification boundary
The C2.1.13 Java experiment is registered in `scripts/verify.sh`. Runtime PASS is established only by executing the suite with Java 21; repository publication alone is prepared evidence, not observed runtime evidence.
