package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.init.items.RockChiselItem;
import com.marbledhubb.antiquus.init.items.RockHammerItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Antiquus.MOD_ID);

    public static final DeferredItem<Item> FOSSILIZED_PROTOTAXITE_SPORES = ITEMS.registerSimpleItem("fossilized_prototaxite_spores");
    public static final DeferredItem<Item> FOSSILIZED_PROTOTAXITE_BUD = ITEMS.registerSimpleItem("fossilized_prototaxite_bud");
    public static final DeferredItem<Item> FOSSILIZED_COOKSONIA = ITEMS.registerSimpleItem("fossilized_cooksonia");
    public static final DeferredItem<Item> FOSSILIZED_ZOSTEROPHYLLUM = ITEMS.registerSimpleItem("fossilized_zosterophyllum");

    // TODO probably change the durability of these two. currently they have the same durability as a brush. it could be interesting if the chisel had a smaller durability -aimi
    public static final DeferredItem<Item> ROCK_HAMMER = ITEMS.registerItem("rock_hammer", RockHammerItem::new, properties -> properties.durability(64));
    public static final DeferredItem<Item> ROCK_CHISEL = ITEMS.registerItem("rock_chisel", RockChiselItem::new, properties -> properties.durability(64));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
