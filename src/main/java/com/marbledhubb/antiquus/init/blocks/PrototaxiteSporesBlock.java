package com.marbledhubb.antiquus.init.blocks;

import com.marbledhubb.antiquus.init.ModBiomes;
import com.marbledhubb.antiquus.init.ModBlockTags;
import com.marbledhubb.antiquus.init.ModBlocks;
import com.marbledhubb.antiquus.init.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public class PrototaxiteSporesBlock extends Block {
    private static final VoxelShape SHAPE = Block.column(16, 0, 1.5);

    public PrototaxiteSporesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void randomTick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, RandomSource random) {
        if (random.nextInt(5) == 0) {
            FillBiomeCommand.fill(
                    level,
                    pos.offset(-5, -5, -5),
                    pos.offset(5, 5, 5),
                    level.registryAccess()
                            .lookupOrThrow(Registries.BIOME)
                            .getOrThrow(ModBiomes.ANCIENT_WETLANDS)
            );
            level.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.get(ModConfiguredFeatures.ANCIENT_SOIL_PATCH)).ifPresent((ancientSoilPatch) -> ancientSoilPatch.value().place(level, level.getChunkSource().getGenerator(), random, pos));
            level.setBlockAndUpdate(pos, ModBlocks.PROTOTAXITE_BUD.get().defaultBlockState());
            BlockState belowState = level.getBlockState(pos.below());
            if (!belowState.is(ModBlockTags.SUPPORTS_PROTOTAXITE) && belowState.is(ModBlockTags.ANCIENT_SOIL_REPLACEABLE))
                level.setBlockAndUpdate(pos.below(), ModBlocks.ANCIENT_SOIL.get().defaultBlockState());
        }
    }

    @Override
    protected @NonNull BlockState updateShape(BlockState state, @NonNull LevelReader level, @NonNull ScheduledTickAccess ticks, @NonNull BlockPos pos, @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state, @NonNull LevelReader level, @NonNull BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        TriState soilDecision = belowState.canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault()) {
            return soilDecision.isTrue();
        } else {
            return belowState.is(ModBlockTags.ANCIENT_SOIL_REPLACEABLE) || belowState.is(ModBlockTags.SUPPORTS_PROTOTAXITE);
        }
    }
}
