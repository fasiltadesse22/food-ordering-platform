# Experiment — Intent Is Not Fact

## Question
Does representing an attempted action as though it were an accomplished fact erase important business reasoning?

## Hypothesis
If command and fact are collapsed, business rejection and unresolved policy become difficult or impossible to represent honestly.

## Prediction
Separating command -> decision -> fact will allow the same requested action to result in acceptance, business rejection, or unresolved policy without claiming the requested outcome happened.

## Setup
Use the dependency-free `OrderingSemantics` lab and executable tests.

## Controlled variable
Decision result for an otherwise explicit business intent.

## Constants
No database, network, message broker, formal Order lifecycle, or concurrency mechanism is introduced.

## Execution
Run `./scripts/verify.sh`.

## Observation
Cancellation intent can be accepted and establish a fact, rejected without a success fact, or left unresolved when lifecycle policy is unknown. Repeating the same stateless command is accepted twice, intentionally demonstrating that command semantics alone do not establish idempotency.

## Evidence
`OrderingSemanticsTest` exercises five semantic cases.

## Interpretation
Intent, decision and established fact are distinct concepts. Repeated-command safety requires additional state/rules/mechanisms not present in this checkpoint.

## Limitations
This lab does not prove persistence, durability, concurrency correctness, idempotency, exactly-once processing, or message-delivery semantics.

## Conclusion
Preserve the semantic distinction before choosing state, transaction, messaging, or retry mechanisms.
