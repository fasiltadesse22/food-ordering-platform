# Assumptions and Unknowns — Part 1.1.1

## Known for the V7 learning baseline
- Customers place food orders with restaurants.
- Restaurant acceptance and rejection exist.
- Payment participates in the workflow.
- Cancellation and refund exist.
- Preparation and completion exist.

## Working assumptions (not permanent truths)
- One initial order references one customer and one restaurant.
- An ordering intent contains at least one requested item.
- Delivery is deferred until a later requirement makes it relevant.

## Unknowns to resolve through later workflow/lifecycle learning
- When does an order become authoritative business state rather than draft intent?
- Does payment occur before or after restaurant acceptance, and is it authorization or capture?
- Until which point is cancellation legal?
- Which modifications remain legal after placement/payment/acceptance?
- What happens when restaurant rejection follows a financial effect?
- What exactly does completion mean?
- Can an order eventually span multiple restaurants?

## Explicitly undecided architecture
- service/microservice boundaries
- aggregate boundaries
- database ownership
- REST vs messaging between future boundaries
- Kafka, Redis, Saga, Outbox, CQRS, Event Sourcing

## Part 1.1.2 refinement
Workflow discovery intentionally keeps these policies open rather than inventing answers:
- payment-first vs restaurant-first vs authorization-then-capture sequencing;
- cancellation legality at each workflow stage;
- the exact financial reversal/release required after rejection or cancellation;
- the precise business definition of completion;
- outcome of concurrent acceptance and cancellation.

## Part 1.1.3 semantic unknowns
- Which state is authoritative when evaluating cancellation and restaurant acceptance?
- Which lifecycle states and guards make each command legal or illegal?
- What durable fact must exist after an accepted decision?
- What identity should a command/operation carry if repeated delivery must be detected?
- Which facts remain internal domain facts and which, if any, later become integration events?
