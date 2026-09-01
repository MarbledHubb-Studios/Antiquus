package com.marbledhubb.antiquus.world.item.crafting;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipePropertySet;

public class ModRecipePropertySets {
    public static final ResourceKey<RecipePropertySet> FOSSIL_RECONSTRUCTION_FOSSIL = create("fossil_reconstruction_fossil");

    private static ResourceKey<RecipePropertySet> create(String name) {
        return ResourceKey.create(RecipePropertySet.TYPE_KEY, Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, name));
    }
}
