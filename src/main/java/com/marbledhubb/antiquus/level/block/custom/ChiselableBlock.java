package com.marbledhubb.antiquus.level.block.custom;

import com.marbledhubb.antiquus.level.block.state.properties.ModBlockStateProperties;
import com.marbledhubb.antiquus.level.block.entity.custom.ChiselableBlockEntity;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ChiselableBlock extends BaseEntityBlock {
    public static final MapCodec<ChiselableBlock> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("turns_into").forGetter(ChiselableBlock::getTurnsInto),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("chisel_sound").forGetter(ChiselableBlock::getChiselSound),
            BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("chisel_completed_sound").forGetter(ChiselableBlock::getChiselCompletedSound),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("chisel_completed_loot_table").forGetter(ChiselableBlock::getChiselCompletedLootTable),
            propertiesCodec()).apply(i, ChiselableBlock::new));
    private static final IntegerProperty CHISELED = ModBlockStateProperties.CHISELED;
    public static final int TICK_DELAY = 2;

    private final Block turnsInto;
    private final SoundEvent chiselSound;
    private final SoundEvent chiselCompletedSound;
    private final ResourceKey<LootTable> chiselCompletedLootTable;

    @Override
    public @NonNull MapCodec<ChiselableBlock> codec() {
        return CODEC;
    }

    public ChiselableBlock(Block turnsInto, SoundEvent chiselSound, SoundEvent chiselCompletedSound, ResourceKey<LootTable> chiselCompletedLootTable, BlockBehaviour.Properties properties) {
        super(properties);
        this.turnsInto = turnsInto;
        this.chiselSound = chiselSound;
        this.chiselCompletedSound = chiselCompletedSound;
        this.chiselCompletedLootTable = chiselCompletedLootTable;
        this.registerDefaultState(this.stateDefinition.any().setValue(CHISELED, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHISELED);
    }

    @Override
    public void onPlace(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, TICK_DELAY);
    }

    @Override
    public @NonNull BlockState updateShape(@NonNull BlockState state, @NonNull LevelReader level, ScheduledTickAccess ticks, @NonNull BlockPos pos, @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        ticks.scheduleTick(pos, this, TICK_DELAY);
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public void tick(@NonNull BlockState state, ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        if (level.getBlockEntity(pos) instanceof ChiselableBlockEntity chiselableBlockEntity) {
            chiselableBlockEntity.checkReset(level);
        }

        if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY()) {
            FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, state);
            entity.disableDrop();
        }

    }

    @Override
    public void animateTick(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, RandomSource random) {
        if (random.nextInt(16) == 0) {
            BlockPos below = pos.below();
            if (FallingBlock.isFree(level.getBlockState(below))) {
                double xx = (double)pos.getX() + random.nextDouble();
                double yy = (double)pos.getY() - 0.05;
                double zz = (double)pos.getZ() + random.nextDouble();
                level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), xx, yy, zz, 0, 0, 0);
            }
        }

    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        ChiselableBlockEntity blockEntity = new ChiselableBlockEntity(pos, state);
        blockEntity.setLootTable(chiselCompletedLootTable, 0L);
        return blockEntity;
    }

    public Block getTurnsInto() {
        return this.turnsInto;
    }

    public SoundEvent getChiselSound() {
        return this.chiselSound;
    }

    public SoundEvent getChiselCompletedSound() {
        return this.chiselCompletedSound;
    }

    public ResourceKey<LootTable> getChiselCompletedLootTable() {
        return chiselCompletedLootTable;
    }
}
