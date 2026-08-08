package com.marbledhubb.antiquus.init.items;

import com.marbledhubb.antiquus.init.ModItemUseAnimations;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
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
    public int getUseDuration(@NonNull ItemStack itemStack, @NonNull LivingEntity user) {
        return 200;
    }
}
