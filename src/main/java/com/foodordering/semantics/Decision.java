package com.foodordering.semantics;

import java.util.List;

/** Result of evaluating an intent against currently modeled knowledge/rules. */
public sealed interface Decision permits Decision.Accepted, Decision.Rejected, Decision.Unresolved {
    record Accepted(List<BusinessFact> establishedFacts) implements Decision {
        public Accepted { establishedFacts = List.copyOf(establishedFacts); }
    }
    record Rejected(String businessReason) implements Decision {
        public Rejected {
            if (businessReason == null || businessReason.isBlank()) throw new IllegalArgumentException("business reason is required");
        }
    }
    record Unresolved(String missingPolicy) implements Decision {
        public Unresolved {
            if (missingPolicy == null || missingPolicy.isBlank()) throw new IllegalArgumentException("missing policy is required");
        }
    }
}
