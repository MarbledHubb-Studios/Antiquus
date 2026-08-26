package com.marbledhubb.antiquus.world.item.crafting.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

public record FossilReconstructionRecipeInput(ItemStack fossil, ItemStack analogue) implements RecipeInput {
    @Override
    public @NonNull ItemStack getItem(int slotIndex) {
        return switch (slotIndex) {
            case 0 -> fossil;
            case 1 -> analogue;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + slotIndex);
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
