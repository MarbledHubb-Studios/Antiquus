package com.marbledhubb.antiquus.world.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.NonNull;

public class RockChiselItem extends Item {
    public RockChiselItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull ItemUseAnimation getUseAnimation(@NonNull ItemStack itemStack) {
        return ModItemUseAnimations.ROCK_CHISEL;
    }

    @Override
    public boolean canPerformAction(@NonNull ItemInstance stack, @NonNull ItemAbility itemAbility) {
        return ModItemAbilities.DEFAULT_ROCK_CHISEL_ACTIONS.contains(itemAbility);
    }
}
