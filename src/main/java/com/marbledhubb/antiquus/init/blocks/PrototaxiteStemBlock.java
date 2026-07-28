package com.marbledhubb.antiquus.init.blocks;

import com.marbledhubb.antiquus.init.ModBlockStateProperties;
import com.marbledhubb.antiquus.init.ModBlockTags;
import com.marbledhubb.antiquus.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.CommonHooks;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PrototaxiteStemBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final IntegerProperty MAX_GROWING_HEIGHT = ModBlockStateProperties.MAX_PROTOTAXITE_STEM_GROWING_HEIGHT;

    private static final int MAX_AGE = 4;
    private static final int MAX_PROTOTAXITE_STEM_LOWER_LIMIT = 2;
    private static final int MAX_PROTOTAXITE_STEM_UPPER_LIMIT = 8;

    public PrototaxiteStemBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, MAX_GROWING_HEIGHT);
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
            if (CommonHooks.canCropGrow(level, above, state, true)) {
                int height = 1;
                int age = state.getValue(AGE);
                int maxGrowingHeight = state.getValue(MAX_GROWING_HEIGHT);

                while (level.getBlockState(pos.below(height)).is(this)) {
                    ++height;
                    if (height == maxGrowingHeight && age == MAX_AGE) {
                        return;
                    }
                }

                if (age == MAX_AGE) {
                    if (height < maxGrowingHeight) {
                        BlockState grownState = state.setValue(AGE, 0);
                        level.setBlockAndUpdate(above, grownState);
                        level.setBlock(pos, grownState, Block.UPDATE_NONE);
                        level.neighborChanged(grownState, above, this, null, false);
                    }
                } else {
                    level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_NONE);
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

    @Override
    public void setPlacedBy(@NonNull Level level, @NonNull BlockPos pos, @NonNull BlockState state, @Nullable LivingEntity by, @NonNull ItemStack itemStack) {
        super.setPlacedBy(level, pos, state, by, itemStack);
        if (!level.isClientSide()) {
            BlockState belowState = level.getBlockState(pos.below());
            if (belowState.is(this)) {
                level.setBlock(pos, state.setValue(MAX_GROWING_HEIGHT, belowState.getValue(MAX_GROWING_HEIGHT)), Block.UPDATE_NONE);
            } else {
                level.setBlock(pos, withRandomMaxGrowingHeight(state, level.getRandom()), Block.UPDATE_NONE);
            }
        }
    }

    public static BlockState withRandomMaxGrowingHeight(BlockState state, RandomSource random) {
        return state.setValue(MAX_GROWING_HEIGHT, random.nextInt(MAX_PROTOTAXITE_STEM_LOWER_LIMIT, MAX_PROTOTAXITE_STEM_UPPER_LIMIT));
    }

    @Override
    public void animateTick(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        BlockState aboveState = level.getBlockState(pos.above());
        if (aboveState.is(this)) return;

        if (random.nextInt(5) == 0) {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            BlockPos relativePos = pos.relative(direction);
            if (!level.getBlockState(relativePos).isSolidRender()) {
                level.addParticle(ModParticles.PROTOTAXITE_SPORE.get(),
                        relativePos.getX() + 0.5 - direction.getStepX() * random.nextDouble(),
                        relativePos.getY() + 0.5 + random.nextDouble() * 2 - 1,
                        relativePos.getZ() + 0.5 - direction.getStepZ() * random.nextDouble(),
                        0,
                        0,
                        0);
            }
        }

        if (aboveState.isAir() && random.nextInt(getAmbientSoundChance(state, level, pos)) == 0)
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FUNGUS_PLACE, SoundSource.AMBIENT, 1.0F, 1.0F, false); // TODO replace with custom sound even -aimi
    }

    private int getAmbientSoundChance(BlockState state, BlockGetter level, BlockPos pos) {
        int height = 1;
        int maxGrowingHeight = state.getValue(MAX_GROWING_HEIGHT);

        while (level.getBlockState(pos.below(height)).is(this)) {
            ++height;
            if (height == maxGrowingHeight) {
                return 130;
            }
        }

        return 90;
    }
}
