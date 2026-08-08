package com.marbledhubb.antiquus.init;

import com.google.common.collect.Sets;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModItemAbilities {
    public static final ItemAbility ROCK_HAMMER_CHISEL = ItemAbility.get("rock_hammer_chisel");

    public static final Set<ItemAbility> DEFAULT_ROCK_HAMMER_ACTIONS = of(ROCK_HAMMER_CHISEL);

    private static Set<ItemAbility> of(ItemAbility... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}
