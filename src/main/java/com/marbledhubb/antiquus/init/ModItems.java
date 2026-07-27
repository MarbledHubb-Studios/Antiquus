package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Antiquus.MODID);

    public static final DeferredItem<Item> FOSSILIZED_PROTOTAXITE_SPORES = ITEMS.registerSimpleItem("fossilized_prototaxite_spores");
    public static final DeferredItem<Item> FOSSILIZED_PROTOTAXITE_BUD = ITEMS.registerSimpleItem("fossilized_prototaxite_bud");
    public static final DeferredItem<Item> FOSSILIZED_COOKSONIA = ITEMS.registerSimpleItem("fossilized_cooksonia");

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
