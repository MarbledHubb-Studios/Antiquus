package com.marbledhubb.antiquus.world.item.crafting;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeBookCategories {
    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES =
            DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, Antiquus.MOD_ID);

    public static final Supplier<RecipeBookCategory> FOSSIL_RECONSTRUCTION = RECIPE_BOOK_CATEGORIES.register("fossil_reconstruction", RecipeBookCategory::new);

    public static void register(IEventBus eventBus) {
        RECIPE_BOOK_CATEGORIES.register(eventBus);
    }
}
