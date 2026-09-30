# Payment / Restaurant Decision Sequencing Alternatives

Part 1.1.2 does not select a final ordering. It exposes the forces.

## Alternative A — Financial step before restaurant acceptance
Potential benefits: financial eligibility known early; restaurant may avoid work for financially invalid orders.
Potential costs: rejection/cancellation can require financial reversal/release; customer may observe a financial effect before fulfillment commitment.

## Alternative B — Restaurant acceptance before financial step
Potential benefits: avoids financial effects for restaurant-rejected orders.
Potential costs: restaurant commitment may be held while payment is attempted; payment failure can invalidate or unwind an accepted commitment.

## Alternative C — Authorization-like hold before acceptance, capture later
Potential benefits: can test financial capability without immediately treating money as final capture.
Potential costs: depends on provider semantics; hold expiry/release and ambiguous outcomes add lifecycle complexity.

## Decision status
OPEN. Precise provider/product semantics are not yet supplied by the roadmap. A later decision must be based on explicit requirements, not assumed industry convention.
