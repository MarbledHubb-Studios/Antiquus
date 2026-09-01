package com.marbledhubb.antiquus.world.item.crafting;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.world.item.crafting.custom.FossilReconstructionRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Antiquus.MOD_ID);

    public static final Supplier<RecipeSerializer<FossilReconstructionRecipe>> RIGHT_CLICK_BLOCK =
            RECIPE_SERIALIZERS.register("fossil_reconstruction", () -> new RecipeSerializer<>(FossilReconstructionRecipe.MAP_CODEC, FossilReconstructionRecipe.STREAM_CODEC));

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
