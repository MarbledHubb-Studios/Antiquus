package com.marbledhubb.antiquus.stats;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStats {
    public static final DeferredRegister<Identifier> STATS =
            DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, Antiquus.MOD_ID);

    public static final DeferredHolder<Identifier, Identifier> INTERACT_WITH_FOSSIL_ANALYSIS_STAND = register("interact_with_fossil_analysis_stand");

    private static DeferredHolder<Identifier, Identifier> register(String name) {
        return STATS.register(name, () -> Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, name));
    }

    public static void register(IEventBus eventBus) {
        STATS.register(eventBus);
    }
}
