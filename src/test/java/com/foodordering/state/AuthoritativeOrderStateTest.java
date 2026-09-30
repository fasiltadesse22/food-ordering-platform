package com.foodordering.state;

import com.foodordering.domain.CustomerId;
import com.foodordering.domain.OrderId;
import com.foodordering.domain.RestaurantId;

import java.util.List;

public final class AuthoritativeOrderStateTest {
    private static int tests;

    public static void main(String[] args) {
        identityRemainsStableWhileRepresentedStateChanges();
        sameAttributesDoNotMakeTwoOrderIdentitiesTheSame();
        detachedSnapshotIsNotAutomaticallyAuthoritative();
        authorityRejectsAccidentalSecondInitialStateForSameIdentity();
        snapshotDoesNotInventLifecycleLegality();
        System.out.println("PASS AuthoritativeOrderStateTest (" + tests + " tests)");
    }

    private static void identityRemainsStableWhileRepresentedStateChanges() {
        var initial = state(OrderId.newId());
        var changed = initial.withFinancialEffectEstablished();
        check(initial.orderId().equals(changed.orderId()), "state change must not create a new business identity");
        check(!initial.financialEffectEstablished() && changed.financialEffectEstablished(), "represented state should change");
        tests++;
    }

    private static void sameAttributesDoNotMakeTwoOrderIdentitiesTheSame() {
        var first = state(OrderId.newId());
        var second = new OrderStateSnapshot(OrderId.newId(), first.customerId(), first.restaurantId(), first.requestedItems(),
                false, false, false, false, false);
        check(!first.orderId().equals(second.orderId()), "distinct order identities remain distinct even with equal descriptive values");
        tests++;
    }

    private static void detachedSnapshotIsNotAutomaticallyAuthoritative() {
        var authority = new InMemoryOrderStateAuthority();
        var initial = state(OrderId.newId());
        authority.establish(initial);
        var observedEarlier = authority.current(initial.orderId());

        authority.replace(initial.withRestaurantAcceptanceEstablished());

        check(!observedEarlier.restaurantAcceptanceEstablished(), "earlier observation should remain stale");
        check(authority.current(initial.orderId()).restaurantAcceptanceEstablished(), "authority should expose the later current state");
        tests++;
    }

    private static void authorityRejectsAccidentalSecondInitialStateForSameIdentity() {
        var authority = new InMemoryOrderStateAuthority();
        var initial = state(OrderId.newId());
        authority.establish(initial);
        try {
            authority.establish(initial.withFinancialEffectEstablished());
            throw new AssertionError("expected duplicate initial establishment to fail");
        } catch (IllegalStateException expected) {
            check(expected.getMessage().contains("already exists"), "failure should explain identity collision");
        }
        tests++;
    }

    private static void snapshotDoesNotInventLifecycleLegality() {
        var contradictory = new OrderStateSnapshot(OrderId.newId(), CustomerId.newId(), RestaurantId.newId(), List.of("Doro Wot"),
                true, true, true, true, true);
        check(contradictory.cancellationEstablished() && contradictory.completionEstablished(),
                "Part 1.1.4 represents state but deliberately does not yet enforce lifecycle legality");
        tests++;
    }

    private static OrderStateSnapshot state(OrderId id) {
        return new OrderStateSnapshot(id, CustomerId.newId(), RestaurantId.newId(), List.of("Doro Wot"),
                false, false, false, false, false);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
