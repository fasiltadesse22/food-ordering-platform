package com.foodordering.domain;

import java.util.List;

public final class EntityIdentitySemanticsTest {
    public static void main(String[] args) {
        sameDescriptionDoesNotMeanSameEntity();
        sameIdentitySurvivesDescriptiveChange();
        separateRepresentationsOfSameIdentityReferToSameEntity();
        typedIdentityValueIsNotTheEntity();
        System.out.println("PASS EntityIdentitySemanticsTest (4 tests)");
    }

    private static void sameDescriptionDoesNotMeanSameEntity() {
        var customer = CustomerId.newId();
        var restaurant = RestaurantId.newId();
        var first = new Order(OrderId.newId(), customer, restaurant, List.of("Doro Wot"));
        var second = new Order(OrderId.newId(), customer, restaurant, List.of("Doro Wot"));

        require(!first.equals(second),
                "descriptively equal orders with different OrderIds are different entities");
    }

    private static void sameIdentitySurvivesDescriptiveChange() {
        var id = OrderId.newId();
        var order = new Order(id, CustomerId.newId(), RestaurantId.newId(), List.of("Doro Wot"));

        order.replaceRequestedItems(List.of("Doro Wot", "Tibs"));

        require(order.id().equals(id), "descriptive change must not replace business identity");
        require(order.requestedItems().equals(List.of("Doro Wot", "Tibs")), "description should change");
    }

    private static void separateRepresentationsOfSameIdentityReferToSameEntity() {
        var id = OrderId.newId();
        var customer = CustomerId.newId();
        var restaurant = RestaurantId.newId();

        var earlierRepresentation = new Order(id, customer, restaurant, List.of("Doro Wot"));
        var laterRepresentation = new Order(id, customer, restaurant, List.of("Tibs"));

        require(earlierRepresentation != laterRepresentation, "these are different Java objects");
        require(earlierRepresentation.equals(laterRepresentation),
                "business identity remains the same despite different representations/state");
    }

    private static void typedIdentityValueIsNotTheEntity() {
        var id = OrderId.newId();
        var order = new Order(id, CustomerId.newId(), RestaurantId.newId(), List.of("Doro Wot"));

        require(!((Object) id).equals(order),
                "OrderId is an identity value; Order is the continuing business entity");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
