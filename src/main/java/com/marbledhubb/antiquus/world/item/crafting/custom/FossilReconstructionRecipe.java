package com.marbledhubb.antiquus.world.item.crafting.custom;

import com.marbledhubb.antiquus.world.item.crafting.ModRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class FossilReconstructionRecipe implements Recipe<FossilReconstructionRecipeInput> {
    public static final MapCodec<FossilReconstructionRecipe> MAP_CODEC =
            RecordCodecBuilder.mapCodec(
                    (i) -> i.group(
                            CommonInfo.MAP_CODEC.forGetter((o) -> o.commonInfo),
                            Ingredient.CODEC.fieldOf("fossil").forGetter((o) -> o.fossil),
                            Ingredient.CODEC.fieldOf("analogue").forGetter((o) -> o.analogue),
                            ItemStackTemplate.CODEC.fieldOf("result").forGetter((o) -> o.result)
                    ).apply(i, FossilReconstructionRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FossilReconstructionRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    CommonInfo.STREAM_CODEC, (o) -> o.commonInfo,
                    Ingredient.CONTENTS_STREAM_CODEC, (o) -> o.fossil,
                    Ingredient.CONTENTS_STREAM_CODEC, (o) -> o.analogue,
                    ItemStackTemplate.STREAM_CODEC, (o) -> o.result,
                    FossilReconstructionRecipe::new);
    public static final RecipeSerializer<FossilReconstructionRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final Recipe.CommonInfo commonInfo;
    private final PlacementInfo placementInfo;

    private final Ingredient fossil;
    private final Ingredient analogue;
    private final ItemStackTemplate result;

    public FossilReconstructionRecipe(Recipe.CommonInfo commonInfo, Ingredient fossil, Ingredient analogue, ItemStackTemplate result) {
        this.commonInfo = commonInfo;
        this.fossil = fossil;
        this.analogue = analogue;
        this.result = result;
        this.placementInfo = PlacementInfo.create(List.of(this.fossil, this.analogue));
    }

    @Override
    public boolean matches(@NonNull FossilReconstructionRecipeInput input, @NonNull Level level) {
        return this.fossil.test(input.fossil()) && this.analogue.test(input.analogue());
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull FossilReconstructionRecipeInput fossilReconstructionRecipeInput) {
        return this.result.create();
    }

    @Override
    public boolean showNotification() {
        return this.commonInfo.showNotification();
    }

    @Override
    public @NonNull String group() {
        return ""; // TODO
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<FossilReconstructionRecipeInput>> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<FossilReconstructionRecipeInput>> getType() {
        return ModRecipeTypes.FOSSIL_RECONSTRUCTION.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return this.placementInfo;
    }

    public Ingredient getFossil() {
        return fossil;
    }

    public ItemStackTemplate getResult() {
        return result;
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return null; // TODO
    }
}
