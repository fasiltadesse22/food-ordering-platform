package com.foodordering.domain;

import java.util.List;

public final class OrderIntentTest {
    public static void main(String[] args) {
        createsIntentForOneCustomerAndOneRestaurant();
        rejectsEmptyRequestedItems();
        System.out.println("PASS OrderIntentTest (2 tests)");
    }

    private static void createsIntentForOneCustomerAndOneRestaurant() {
        var intent = new OrderIntent(OrderId.newId(), CustomerId.newId(), RestaurantId.newId(), List.of("Doro Wot"));
        require(intent.requestedItems().equals(List.of("Doro Wot")), "requested item should be preserved");
    }

    private static void rejectsEmptyRequestedItems() {
        try {
            new OrderIntent(OrderId.newId(), CustomerId.newId(), RestaurantId.newId(), List.of());
            throw new AssertionError("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            require(expected.getMessage().contains("at least one"), "error should explain the business input problem");
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
