package com.marbledhubb.antiquus.init.blocks;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

public class SuspiciousStoneBlock extends BrushableBlock {

    public static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(Antiquus.MODID, "archaeology/suspicious_stone"));

    public SuspiciousStoneBlock(Properties properties) {
        super(Blocks.STONE, SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        BrushableBlockEntity blockEntity = new BrushableBlockEntity(pos, state);
        blockEntity.setLootTable(LOOT_TABLE, 0L);
        return blockEntity;
    }

    @Override
    public SoundEvent getBrushCompletedSound() {
        return SoundEvents.STONE_BREAK;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

}
