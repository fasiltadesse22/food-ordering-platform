package com.foodordering.correctness;

public final class CandidateAggregateDerivationTest {
    public static void main(String[] args) {
        comparesMultipleCandidates();
        preservesCrossBoundaryPressure();
        rejectsNounToAggregateShortcut();
        includesTooSmallAndTooLargeCounterexamples();
        System.out.println("PASS CandidateAggregateDerivationTest (4 tests)");
    }

    private static void comparesMultipleCandidates() {
        require(CandidateAggregateCatalog.all().size() >= 4,
                "P04 must compare alternatives rather than assert one boundary");
    }

    private static void preservesCrossBoundaryPressure() {
        var separate = CandidateAggregateCatalog.orderAndPaymentSeparate();
        require(separate.invariantsCrossingBoundary().stream()
                        .anyMatch(i -> i.contains("acceptance")),
                "separate Order/Payment candidate must preserve stale cross-fact pressure");
    }

    private static void rejectsNounToAggregateShortcut() {
        var oversized = CandidateAggregateCatalog.wholeOrderingGraph();
        require(oversized.risks().stream().anyMatch(r -> r.contains("relationship/navigation")),
                "object relationship must not be treated as consistency evidence");
    }

    private static void includesTooSmallAndTooLargeCounterexamples() {
        require(CandidateAggregateCatalog.fragmentedOrder().status().contains("undersized"),
                "must preserve undersized alternative");
        require(CandidateAggregateCatalog.wholeOrderingGraph().status().contains("oversized"),
                "must preserve oversized alternative");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
