# Experiment — Happy Path Only vs Alternative-Path Discovery

## Question
Does modeling only the successful path expose enough information to make architecture decisions?

## Hypothesis
No. Alternative paths expose decision authority, sequencing, reversal, concurrency, and consistency questions hidden by the happy path.

## Prediction
Adding restaurant rejection after a financial step and cancellation before restaurant decision will create unresolved policy questions that cannot be answered by an endpoint/service diagram.

## Setup
Compare the successful-fulfillment workflow with the executable rejection and cancellation scenarios in `OrderingWorkflowCatalog`.

## Controlled variable
Workflow path (successful vs rejection/cancellation alternative).

## Constants
Same initial actors and one-customer/one-restaurant baseline assumptions from Part 1.1.1.

## Observation
The alternative paths require explicit unresolved-policy steps for financial reversal/release, cancellation legality, and interaction with a restaurant decision.

## Evidence
`OrderingWorkflowCatalogTest` verifies those unresolved policy points remain represented instead of being silently guessed.

## Interpretation
Workflow discovery precedes mechanism selection because important architectural forces are concentrated in branches, conflicts, and consequences rather than only the happy path.

## Limitations
This experiment does not establish the correct cancellation rule, payment semantics, concurrency outcome, transaction boundary, or service boundary.

## Conclusion
Do not select Saga, distributed locking, messaging, or service boundaries merely from the happy path. First make the workflow branches and unresolved decisions explicit.
