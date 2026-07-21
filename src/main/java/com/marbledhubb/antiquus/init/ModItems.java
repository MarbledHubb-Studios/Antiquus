package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Antiquus.MODID);

    public static final DeferredItem<Item> PROTOTAXITE_STEM = ITEMS.registerItem("prototaxite_stem", properties -> new BlockItem(ModBlocks.PROTOTAXITE_STEM.get(), properties.useBlockDescriptionPrefix()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
