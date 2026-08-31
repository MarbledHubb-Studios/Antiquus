package com.marbledhubb.antiquus.world.item;

import com.google.common.collect.Sets;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModItemAbilities {
    public static final ItemAbility ROCK_HAMMER_CHISEL = ItemAbility.get("rock_hammer_chisel");
    public static final ItemAbility ROCK_CHISEL_CHISEL = ItemAbility.get("rock_chisel_chisel");

    public static final Set<ItemAbility> DEFAULT_ROCK_HAMMER_ACTIONS = of(ROCK_HAMMER_CHISEL);
    public static final Set<ItemAbility> DEFAULT_ROCK_CHISEL_ACTIONS = of(ROCK_CHISEL_CHISEL);

    private static Set<ItemAbility> of(ItemAbility... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}
