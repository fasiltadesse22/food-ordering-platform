# C2.1.13 — Staff-Level Design Defense

## Position to defend
The current Food Ordering Platform is intentionally a correctness-discovery model, not yet a production architecture. Its value is that later boundaries and mechanisms can be derived from explicit business invariants and demonstrated failure windows.

## Defense 1 — Why not define aggregates immediately?
Because aggregate boundaries are supposed to protect invariants and define a consistency boundary. Cluster 1.1 first discovers the workflows, authoritative facts, lifecycle, invariants, and conflicting operations. Choosing an aggregate before this evidence would risk encoding structural guesses as domain truth.

## Defense 2 — Why is a passing guard test insufficient?
INV-ORDER-01 demonstrates the distinction. A guarded path can reject paid-order modification while another mutation path bypasses the guard. Therefore the invariant is the specification; the guard is only one enforcement point. Later architecture must make authoritative mutation respect the invariant comprehensively.

## Defense 3 — Why is duplicate payment not just a request-ID problem?
Different request/attempt IDs can represent the same logical payment intent. Correctness is defined over logical business identity: at most one successful effect per logical payment. Transport identity alone cannot establish that property.

## Defense 4 — Why are sequential lifecycle tests insufficient?
A transition can be legal when evaluated against a snapshot and stale when applied. C2.1.10/C2.1.11 expose read/decide/write and observe/decide/delay/apply windows. Sequential legality is necessary but does not prove concurrent correctness.

## Defense 5 — Why not solve the race with a lock now?
The evidence establishes a missing guarantee, not the correct mechanism. Mechanism selection requires the eventual ownership boundary, persistence model, conflict frequency, workload, latency goals, failure model, and operational cost. Premature locking would collapse problem discovery into implementation habit.

## Defense 6 — Why does cross-fact consistency matter before microservices?
Distribution is not required for the semantic problem. If an invariant depends on order truth and payment truth that can change independently, local order legality alone cannot prove the combined invariant. Service decomposition can later make that coordination more expensive, but it does not create the underlying business dependency.

## Defense 7 — What is the strongest current claim?
The project has executable semantic models and controlled experiments for the specified correctness concerns. Repository publication establishes prepared evidence. Runtime guarantees are claimed only after execution under the stated conditions, and production guarantees require stronger mechanisms and evidence.

## Adversarial follow-ups
**“Would you use Saga?”** Not derivable yet. First determine whether temporary divergence is business-legal, which effects are reversible, and whether one local consistency boundary can protect the invariant.

**“Would you use optimistic locking?”** It is a later candidate for stale-write/conflict detection, not a conclusion of this cluster.

**“Should Payment be a separate service?”** Service ownership cannot be justified from nouns alone. Frequency and strength of cross-boundary invariants, independent change/deployment needs, workload, failure coupling, and operational constraints must be evaluated.

**“Can Kafka make payment exactly once?”** The current invariant is about one successful business effect per logical payment. Delivery semantics and business-effect idempotency are different problems; Kafka is deliberately outside this cluster.

## Architectural handoff
The next design work must consume, not replace, this evidence:
- model identities and value semantics;
- derive aggregate candidates from invariant scope;
- decide ownership of authoritative mutation;
- define repository/local transaction boundaries;
- evaluate concurrency control against demonstrated race windows;
- preserve explicit non-guarantees until evidence upgrades them.
