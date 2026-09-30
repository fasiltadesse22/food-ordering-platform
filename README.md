# Food Ordering Platform — Course 2

Continuously evolving learning project for **Enterprise Distributed Systems Architecture & System Design**.

This repository is intentionally evolved from business/domain reasoning toward distributed mechanisms only when architectural pressure and evidence justify them.

## Current checkpoint — C2.1.6

The project currently covers:

- business actors, goals and ordering intent;
- workflow discovery and command/decision/fact semantics;
- identity and authoritative-state reasoning;
- explicit Order lifecycle and legal/illegal transitions;
- selected business invariants translated into executable correctness claims;
- evidence that distinguishes sequentially observed behavior from unproven concurrency, durability and distributed guarantees.

No Kafka, Redis, Saga, Outbox, CQRS, microservice decomposition, or distributed coordination is introduced yet. Those mechanisms must earn their place through later requirements and evidence.

## Verify

Use Java 21:

```bash
./scripts/verify.sh
```

The verification script compiles main and test sources and runs the executable learning suites.
