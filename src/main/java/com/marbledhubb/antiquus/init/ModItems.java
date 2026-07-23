package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(Antiquus.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
