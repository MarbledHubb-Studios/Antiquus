package com.marbledhubb.antiquus.world.level.block.custom;

import com.marbledhubb.antiquus.data.worldgen.features.ModConfiguredFeatures;
import com.marbledhubb.antiquus.tags.ModBlockTags;
import com.marbledhubb.antiquus.world.level.biome.ModBiomes;
import com.marbledhubb.antiquus.world.level.block.ModBlocks;
import com.marbledhubb.antiquus.world.level.saveddata.BiomeOverrides;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class FertilePrototaxiteSporesBlock extends PrototaxiteSporesBlock {
    public FertilePrototaxiteSporesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void grow(ServerLevel level, BlockPos pos, RandomSource random) {
        BiomeOverrides.add(
                level,
                pos,
                level.registryAccess()
                        .lookupOrThrow(Registries.BIOME)
                        .getOrThrow(ModBiomes.ANCIENT_WETLANDS),
                pos.offset(-3, -3, -3),
                pos.offset(3, 3, 3)
        );
        level.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.get(ModConfiguredFeatures.SILURIAN_PATCH)).ifPresent((silurianPatch) -> silurianPatch.value().place(level, level.getChunkSource().getGenerator(), random, pos));

        BlockState belowState = level.getBlockState(pos.below());
        if (!belowState.is(ModBlockTags.SUPPORTS_PROTOTAXITE_GROWTH) && belowState.is(ModBlockTags.SILURIAN_REPLACEABLE))
            level.setBlockAndUpdate(pos.below(), ModBlocks.ANCIENT_SOIL.get().defaultBlockState());

        super.grow(level, pos, random);
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state, @NonNull LevelReader level, @NonNull BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        TriState soilDecision = belowState.canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault()) {
            return soilDecision.isTrue();
        } else {
            return belowState.is(ModBlockTags.SILURIAN_REPLACEABLE) || belowState.is(ModBlockTags.SUPPORTS_PROTOTAXITE_GROWTH);
        }
    }
}
