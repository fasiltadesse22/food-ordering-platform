package com.foodordering.correctness;

import java.util.List;

/**
 * P03 evidence catalog derived from Cluster 1.1 invariants.
 *
 * These are dependency descriptions, not final ownership decisions. In
 * particular, labels such as "order lifecycle authority" and "payment-effect
 * authority" describe facts that are authoritative in the current learning
 * model; they do NOT imply Aggregates, services, tables or deployments.
 */
public final class InvariantStateDependencyCatalog {
    private InvariantStateDependencyCatalog() {}

    public static List<InvariantStateDependency> all() {
        return List.of(paidCommercialAgreement(), oneSuccessfulPaymentEffect(),
                exclusiveOrderOutcome(), refundCeiling());
    }

    public static InvariantStateDependency paidCommercialAgreement() {
        return new InvariantStateDependency(
                "INV-ORDER-01",
                "payment-consequential commercial terms are not silently rewritten",
                List.of(
                        fact("payment consequence/lifecycle state", "current order/payment learning authorities",
                                "payment/lifecycle progression can make terms consequential"),
                        fact("current commercial terms", "current Order representation",
                                "requested commercial terms can currently be replaced"),
                        fact("proposed commercial terms", "incoming business intent",
                                "caller proposes replacement values")
                ),
                List.of(conflict("establish payment consequence", "replace commercial terms",
                        "either ordering can make a previously legal local action violate the combined business truth")),
                List.of("payment consequence established + silently replaced quantity/price/restaurant/order-line terms")
        );
    }

    public static InvariantStateDependency oneSuccessfulPaymentEffect() {
        return new InvariantStateDependency(
                "INV-PAYMENT-01",
                "successfulEffects(logicalPaymentId) <= 1",
                List.of(
                        fact("logical payment identity", "payment-intent semantics",
                                "requests/attempts can refer to the same logical payment"),
                        fact("successful-effect existence/count", "CorrectnessModel in-memory learning authority",
                                "successful effect can be checked then recorded"),
                        fact("proposed successful effect", "current payment attempt/result",
                                "a new success can be proposed")
                ),
                List.of(conflict("observe no successful effect", "record another successful effect",
                        "two actors can make the same decision from the same prior observation")),
                List.of("two successful business effects for one logical payment identity")
        );
    }

    public static InvariantStateDependency exclusiveOrderOutcome() {
        return new InvariantStateDependency(
                "INV-ORDER-02",
                "mutually exclusive final outcomes are not both authoritative",
                List.of(
                        fact("order identity", "OrderId value identifying the continuing Order",
                                "identity is stable while outcomes are decided"),
                        fact("current authoritative lifecycle state", "order lifecycle authority",
                                "accept/cancel transitions change authoritative lifecycle truth"),
                        fact("proposed outcome", "incoming restaurant/customer decision",
                                "acceptance or cancellation can be proposed")
                ),
                List.of(conflict("accept order", "cancel order",
                        "both may be legal decisions from the same stale prior state but cannot both become authoritative")),
                List.of("restaurant acceptance authoritative + cancellation authoritative for the same exclusive lifecycle point")
        );
    }

    public static InvariantStateDependency refundCeiling() {
        return new InvariantStateDependency(
                "INV-REFUND-01",
                "cumulative successful refund does not exceed eligible captured value",
                List.of(
                        fact("payment identity", "payment-domain specification only",
                                "identifies which captured value/refunds belong together"),
                        fact("eligible captured value", "payment/refund specification only",
                                "capture/refund policy can change eligibility"),
                        fact("cumulative successful refunds", "payment/refund specification only",
                                "each successful refund increases cumulative value"),
                        fact("proposed refund amount", "incoming refund intent",
                                "caller proposes another refund")
                ),
                List.of(conflict("approve/refund amount A", "approve/refund amount B",
                        "independent approvals can each fit remaining eligibility while their combined value exceeds it")),
                List.of("sum(successful refunds) > eligible captured value")
        );
    }

    private static InvariantStateDependency.RequiredFact fact(String name, String authority, String mutation) {
        return new InvariantStateDependency.RequiredFact(name, authority, mutation);
    }

    private static InvariantStateDependency.ConflictingMutation conflict(String first, String second, String reason) {
        return new InvariantStateDependency.ConflictingMutation(first, second, reason);
    }
}
