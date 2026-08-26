package com.marbledhubb.antiquus.mixin;

import com.marbledhubb.antiquus.world.item.crafting.ModRecipePropertySets;
import com.marbledhubb.antiquus.world.item.crafting.custom.FossilReconstructionRecipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> RECIPE_PROPERTY_SETS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addModPropertySets(CallbackInfo ci) {
        var map = new HashMap<>(RECIPE_PROPERTY_SETS);

        map.put(
                ModRecipePropertySets.FOSSIL_RECONSTRUCTION_FOSSIL,
                recipe -> recipe instanceof FossilReconstructionRecipe fossilReconstructionRecipe ? Optional.of(fossilReconstructionRecipe.placementInfo().ingredients().getFirst()) : Optional.empty()
        );

        RECIPE_PROPERTY_SETS = Map.copyOf(map);
    }
}
