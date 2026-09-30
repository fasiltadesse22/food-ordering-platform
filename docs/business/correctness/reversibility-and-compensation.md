# C2.1.8 — Reversible, Irreversible and Compensatable Business Actions

## Purpose
Classify consequential workflow actions by what can actually be undone. This prevents later architecture from treating rollback, reversal and compensation as synonyms.

## Classification vocabulary

### Reversible
The authoritative business state can be restored without leaving a consequential external effect that requires a separate business action.

Example in the current model: editing a draft-like commercial proposal before payment has become consequential, subject to the applicable lifecycle rules.

### Irreversible
The original fact cannot be made historically false once it has occurred. A successful external charge is the canonical example: a later refund does not mean the charge never happened.

### Compensatable
The original fact remains true, but a new business action can offset some or all of its consequences.

Example: `PaymentSucceeded` followed later by `RefundSucceeded`. The refund is not rollback of the charge; it is a new fact with its own eligibility, failure modes and audit meaning.

## Food Ordering action classification

| Action/fact | Classification | Why | Possible later action |
|---|---|---|---|
| change unpaid/editable order terms | reversible within current policy | no successful payment effect is being erased | restore prior proposal if business permits |
| successful payment effect | irreversible fact; financially compensatable | money movement/effect happened and cannot be made historically false | refund workflow |
| restaurant acceptance | consequential and not safely modeled as simple rollback | restaurant may allocate capacity/start downstream work | explicit cancellation/restaurant-release workflow if policy permits |
| start preparation | operationally consequential; treated as non-reversible in current lifecycle | ingredients/labor/time may already be consumed | separate exception/remediation policy, not transition back |
| cancellation | terminal ordering fact in current model | restoring the prior state would erase business history | a new order/reinstatement workflow would require explicit semantics |
| refund success | irreversible financial fact | refund itself happened | further adjustment requires another explicit financial action |

## History matters

`A -> B -> A` is not necessarily equivalent to "nothing happened".

For consequential operations the system may need to preserve facts such as:

`PaymentSucceeded -> CancellationAccepted -> RefundSucceeded`

rather than rewriting history to look like payment never occurred.

## Compensation correctness questions

For every compensatable action ask:
1. What original fact remains true?
2. What new command requests compensation?
3. Who is allowed to decide it?
4. What state establishes eligibility?
5. Can compensation fail, time out, or be retried?
6. Is compensation partial or complete?
7. What invariant limits the compensation? (`INV-REFUND-01` is one example.)
8. What must operators/users observe while compensation is pending?

## Explicit non-guarantees
This part does not implement Saga, distributed transactions, payment-provider integration, durable refund execution, aggregate boundaries, repositories, or ACID transaction design. It classifies business semantics so later mechanisms can be selected for the right reason.
