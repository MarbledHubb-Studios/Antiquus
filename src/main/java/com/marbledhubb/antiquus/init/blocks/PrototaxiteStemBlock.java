package com.marbledhubb.antiquus.init.blocks;

import com.marbledhubb.antiquus.data.BiomeOverrides;
import com.marbledhubb.antiquus.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PrototaxiteStemBlock extends Block implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    public static final IntegerProperty MAX_GROWING_HEIGHT = ModBlockStateProperties.MAX_PROTOTAXITE_STEM_GROWING_HEIGHT;

    public static final ThreadLocal<Direction> BONEMEALED_FACE = new ThreadLocal<>();

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
    protected void randomTick(@NonNull BlockState state, ServerLevel level, BlockPos pos, @NonNull RandomSource random) {
        BlockPos above = pos.above();
        if (level.isEmptyBlock(above)) {
            if (CommonHooks.canCropGrow(level, above, state, true)) {
                int height = 1;
                int age = state.getValue(AGE);
                int maxGrowingHeight = state.getValue(MAX_GROWING_HEIGHT);

                while (true) {
                    BlockState belowState = level.getBlockState(pos.below(height));
                    if (!belowState.is(this)) {
                        if (belowState.is(ModBlockTags.SUPPORTS_PROTOTAXITE_GROWTH)) {
                            break;
                        } else {
                            return;
                        }
                    }

                    if (++height == maxGrowingHeight && age == MAX_AGE) {
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
    public boolean isValidBonemealTarget(@NonNull LevelReader levelReader, @NonNull BlockPos pos, @NonNull BlockState state) {
        return canEmitSpores(levelReader, pos);
    }

    @Override
    public boolean isBonemealSuccess(@NonNull Level level, RandomSource random, @NonNull BlockPos pos, @NonNull BlockState state) {
        return random.nextInt(5) < 2;
    }

    @Override
    public void performBonemeal(@NonNull ServerLevel level, @NonNull RandomSource random, @NonNull BlockPos pos, @NonNull BlockState state) {
        Direction face = BONEMEALED_FACE.get();
        Vec3 itemPos = Vec3.atCenterOf(pos).add(face.getUnitVec3().scale(0.51)).add(randomOffsetAlongFace(level.getRandom(), face, 0.7));

        ItemEntity entity = new ItemEntity(level, itemPos.x(), itemPos.y(), itemPos.z(), ModBlocks.PROTOTAXITE_SPORES.toStack());
        entity.setDefaultPickUpDelay();
        level.addFreshEntity(entity);
    }

    private static Vec3 randomOffsetAlongFace(RandomSource random, Direction face, double amount) {
        double x = 0, y = 0, z = 0;

        switch (face.getAxis()) {
            case X -> {
                y = (random.nextDouble() - 0.5) * amount;
                z = (random.nextDouble() - 0.5) * amount;
            }
            case Y -> {
                x = (random.nextDouble() - 0.5) * amount;
                z = (random.nextDouble() - 0.5) * amount;
            }
            case Z -> {
                x = (random.nextDouble() - 0.5) * amount;
                y = (random.nextDouble() - 0.5) * amount;
            }
        }

        return new Vec3(x, y, z);
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, boolean movedByPiston) {
        BiomeOverrides.remove(level, pos);
    }

    @Override
    public @NonNull BlockPos getParticlePos(@NonNull BlockPos pos) {
        return pos.relative(BONEMEALED_FACE.get());
    }

    @Override
    public void animateTick(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        BlockState aboveState = level.getBlockState(pos.above());
        if (!canEmitSpores(level, pos, aboveState)) return;

        if (random.nextInt(5) == 0)
            level.addParticle(ModParticles.PROTOTAXITE_SPORE.get(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);

        if (aboveState.isAir() && random.nextInt(getAmbientSoundChance(state, level, pos)) == 0)
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.PROTOTAXITE_STEM_CREAKING_AMBIENCE.get(), SoundSource.AMBIENT, 0.8F, 1.4F / (level.getRandom().nextFloat() + 0.2F), false);
    }

    private boolean canEmitSpores(LevelReader levelReader, BlockPos pos) {
        return canEmitSpores(levelReader, pos, levelReader.getBlockState(pos.above()));
    }

    private boolean canEmitSpores(LevelReader levelReader, BlockPos pos, BlockState aboveState) {
        if (aboveState.is(this)) return false;

        int height = 1;
        while (true) {
            BlockState belowState = levelReader.getBlockState(pos.below(height));
            if (!belowState.is(this)) {
                if (belowState.is(ModBlockTags.SUPPORTS_PROTOTAXITE_GROWTH)) {
                    break;
                } else {
                    return false;
                }
            }
            height++;
        }

        return true;
    }

    private int getAmbientSoundChance(BlockState state, BlockGetter level, BlockPos pos) {
        int height = 1;
        int maxGrowingHeight = state.getValue(MAX_GROWING_HEIGHT);

        while (level.getBlockState(pos.below(height)).is(this)) {
            ++height;
            if (height == maxGrowingHeight) {
                return 150;
            }
        }

        return 110;
    }
}
