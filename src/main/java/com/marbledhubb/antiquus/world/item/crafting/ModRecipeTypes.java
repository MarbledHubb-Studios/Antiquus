package com.marbledhubb.antiquus.world.item.crafting;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.world.item.crafting.custom.FossilReconstructionRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Antiquus.MOD_ID);

    public static final Supplier<RecipeType<FossilReconstructionRecipe>> FOSSIL_RECONSTRUCTION = RECIPE_TYPES.register("fossil_reconstruction", RecipeType::simple);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
