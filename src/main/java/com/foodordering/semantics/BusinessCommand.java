package com.foodordering.semantics;

/**
 * An intent to attempt a business change. A command is not evidence that the
 * requested outcome occurred.
 */
public record BusinessCommand(String name, String targetId, String requestedBy) {
    public BusinessCommand {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("command name is required");
        if (targetId == null || targetId.isBlank()) throw new IllegalArgumentException("target id is required");
        if (requestedBy == null || requestedBy.isBlank()) throw new IllegalArgumentException("requester is required");
    }
}
