package com.foodordering.lifecycle;

/** Commands/facts are richer than transitions; this enum names only lifecycle movement. */
public enum OrderTransition {
    ESTABLISH_PAYMENT,
    ACCEPT_BY_RESTAURANT,
    REJECT_BY_RESTAURANT,
    START_PREPARATION,
    COMPLETE_ORDER,
    CANCEL_ORDER
}
