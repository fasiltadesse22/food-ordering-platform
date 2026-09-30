#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$ROOT/target/manual-verify"
rm -rf "$OUT"
mkdir -p "$OUT"
find "$ROOT/src/main/java" "$ROOT/src/test/java" -name '*.java' -print0 | xargs -0 javac --release 21 -d "$OUT"
java -ea -cp "$OUT" com.foodordering.domain.OrderIntentTest
java -ea -cp "$OUT" com.foodordering.workflow.OrderingWorkflowCatalogTest
java -ea -cp "$OUT" com.foodordering.semantics.OrderingSemanticsTest
java -ea -cp "$OUT" com.foodordering.state.AuthoritativeOrderStateTest
java -ea -cp "$OUT" com.foodordering.lifecycle.OrderLifecycleTest
echo "PASS compile main + test sources with Java 21"
