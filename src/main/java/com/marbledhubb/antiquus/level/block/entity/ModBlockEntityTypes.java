package com.marbledhubb.antiquus.level.block.entity;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.level.block.ModBlocks;
import com.marbledhubb.antiquus.level.block.entity.custom.ChiselableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Antiquus.MOD_ID);

    public static final Supplier<BlockEntityType<ChiselableBlockEntity>> CHISELABLE_BLOCK = BLOCK_ENTITY_TYPES.register(
            "chiselable_block",
            () -> new BlockEntityType<>(ChiselableBlockEntity::new, ModBlocks.SUSPICIOUS_STONE.get())
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
