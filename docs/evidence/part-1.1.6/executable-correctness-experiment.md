# Part 1.1.6 — Executable Correctness Experiment

## Question
Can selected business truths be represented as executable claims without confusing the claims with their current enforcement mechanisms?

## Hypothesis
Tests should reject a silent commercial change after payment, a second successful effect for the same logical payment, and mutually conflicting order outcomes. They should also expose that sequential correctness does not prove concurrent correctness.

## Prediction
The correctness suite passes for sequential scenarios. Inspection of the payment mechanism reveals a `contains` then `add` check-and-act window, so no concurrency guarantee can legitimately be claimed.

## Setup
C2.1.5 lifecycle model plus a small C2.1.6 correctness package and executable Java assertion suite.

## Controlled variable
Business operation sequence: legal proposal, duplicate logical effect, or conflicting transition.

## Constants
Java 21; single process; no database; no Kafka; no Redis; no distributed services; no persistence transaction.

## Execution
Run `./scripts/verify.sh`.

## Evidence
- invariant IDs and catalog;
- executable assertions;
- lifecycle transition policy;
- explicit check-then-act code for logical payment success;
- verification output.

## Interpretation
A passing sequential test proves only the tested behavior under the stated setup. It does not prove thread safety, crash safety, durable uniqueness, exactly-once processing, or correctness after distribution.

## Limitations
The experiment deliberately lacks concurrent threads, persistence, transaction isolation, retries across a network, process crashes, and recovery. Those missing conditions are future architectural pressures, not accidental omissions.

## Conclusion
The project now has executable correctness claims that can survive architectural evolution as specifications. The next learning step can use them to ask what boundaries and mechanisms are required to preserve those truths under concurrency and persistence.
