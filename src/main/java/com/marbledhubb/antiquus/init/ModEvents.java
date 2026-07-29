package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

@EventBusSubscriber(modid = Antiquus.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onAddBlockEntityType(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityTypes.BRUSHABLE_BLOCK, ModBlocks.SUSPICIOUS_STONE.get());
    }
}