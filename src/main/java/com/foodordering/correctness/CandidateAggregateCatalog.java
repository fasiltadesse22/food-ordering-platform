package com.foodordering.correctness;

import java.util.List;

/**
 * P04 compares plausible and deliberately weak candidate boundaries.
 * None is a final production selection. P05+ must still derive root/mutation
 * authority, P07 consistency semantics and P08 transaction alignment.
 */
public final class CandidateAggregateCatalog {
    private CandidateAggregateCatalog() {}

    public static List<CandidateAggregateDesign> all() {
        return List.of(
                orderAndPaymentSeparate(),
                orderPaymentCombined(),
                fragmentedOrder(),
                wholeOrderingGraph()
        );
    }

    public static CandidateAggregateDesign orderAndPaymentSeparate() {
        return new CandidateAggregateDesign(
                "A: Order consistency unit + Payment consistency unit",
                List.of(
                        "Order: identity + lifecycle + commercial terms",
                        "Payment: logical payment identity + successful-effect accounting + refund accounting"
                ),
                List.of("INV-ORDER-01 (subject to how payment consequence is represented)", "INV-ORDER-02",
                        "INV-PAYMENT-01", "INV-REFUND-01"),
                List.of("acceptance rule requiring current Order lifecycle and current Payment truth"),
                List.of("keeps independent Order and Payment lifecycles explicit",
                        "localizes payment/refund accounting pressure",
                        "avoids coupling unrelated payment mutations to every Order mutation"),
                List.of("cross-boundary stale-decision window remains explicit",
                        "INV-ORDER-01 needs careful representation of payment consequence"),
                "plausible candidate; requires later cross-boundary reasoning"
        );
    }

    public static CandidateAggregateDesign orderPaymentCombined() {
        return new CandidateAggregateDesign(
                "B: Combined Order + Payment consistency unit",
                List.of("Order identity/lifecycle/commercial terms + logical Payment financial state"),
                List.of("INV-ORDER-01", "INV-ORDER-02", "INV-PAYMENT-01", "INV-REFUND-01",
                        "Order acceptance requiring current Payment truth"),
                List.of(),
                List.of("can make inherited Order/Payment correctness relationships locally visible",
                        "reduces the specific stale cross-fact window inside the candidate boundary"),
                List.of("couples Order and Payment lifecycles",
                        "larger mutation/conflict surface",
                        "potential contention and transaction growth",
                        "external payment effects may still escape any local boundary"),
                "plausible but intentionally challenged as potentially oversized"
        );
    }

    public static CandidateAggregateDesign fragmentedOrder() {
        return new CandidateAggregateDesign(
                "C: Lifecycle and commercial terms as independent consistency units",
                List.of("Order lifecycle state", "commercial terms", "Payment financial state"),
                List.of("INV-PAYMENT-01", "INV-REFUND-01"),
                List.of("INV-ORDER-01", "INV-ORDER-02 may require reconstructed Order authority",
                        "Order acceptance + Payment truth"),
                List.of("small independently mutable units"),
                List.of("splits facts that inherited Order invariants need together",
                        "creates coordination for ordinary Order mutations",
                        "weakens one authoritative Order lifecycle model"),
                "deliberately undersized counterexample"
        );
    }

    public static CandidateAggregateDesign wholeOrderingGraph() {
        return new CandidateAggregateDesign(
                "D: Whole ordering graph consistency unit",
                List.of("Customer + Order + Restaurant + Payment + refund-related state"),
                List.of("all currently catalogued invariants"),
                List.of(),
                List.of("many current rules could be evaluated with one conceptual state graph"),
                List.of("includes state not required by the known invariant closures",
                        "couples independent identities/lifecycles",
                        "maximizes contention and change coupling",
                        "mistakes relationship/navigation for consistency requirement"),
                "deliberately oversized counterexample"
        );
    }
}
