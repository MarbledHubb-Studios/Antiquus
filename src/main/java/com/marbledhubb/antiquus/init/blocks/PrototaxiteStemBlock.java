package com.marbledhubb.antiquus.init.blocks;

import com.marbledhubb.antiquus.init.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.CommonHooks;
import org.jspecify.annotations.NonNull;

public class PrototaxiteStemBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    public static final int MAX_AGE = 7;
    private static final int MAX_PROTOTAXITE_STEM_GROWING_HEIGHT = 10;

    public PrototaxiteStemBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected void tick(@NonNull BlockState state, ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }
        }
    }

    @Override
    protected void randomTick(@NonNull BlockState state, ServerLevel level, BlockPos pos, @NonNull RandomSource random) {
        BlockPos above = pos.above();
        if (level.isEmptyBlock(above)) {
            int height = 1;
            int age = state.getValue(AGE);
            if (CommonHooks.canCropGrow(level, above, state, true)) {
                while (level.getBlockState(pos.below(height)).is(this)) {
                    ++height;
                    if (height == MAX_PROTOTAXITE_STEM_GROWING_HEIGHT && age == MAX_AGE) {
                        return;
                    }
                }

                if (age == MAX_AGE && height < MAX_PROTOTAXITE_STEM_GROWING_HEIGHT) {
                    level.setBlockAndUpdate(above, this.defaultBlockState());
                    BlockState aboveBlock = state.setValue(AGE, 0);
                    level.setBlock(pos, aboveBlock, 260);
                    level.neighborChanged(aboveBlock, above, this, null, false);
                }

                if (age < MAX_AGE) {
                    level.setBlock(pos, state.setValue(AGE, age + 1), 260);
                }

                CommonHooks.fireCropGrowPost(level, pos, state);
            }
        }
    }

    @Override
    protected @NonNull BlockState updateShape(BlockState state, @NonNull LevelReader level, @NonNull ScheduledTickAccess ticks, @NonNull BlockPos pos, @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            ticks.scheduleTick(pos, this, 1);
        }

        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state, @NonNull LevelReader level, @NonNull BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        TriState soilDecision = belowState.canSustainPlant(level, pos.below(), Direction.UP, state);
        if (!soilDecision.isDefault()) {
            return soilDecision.isTrue();
        } else {
            return (belowState.is(this) || belowState.is(ModBlockTags.SUPPORTS_PROTOTAXITE));
        }
    }
}
