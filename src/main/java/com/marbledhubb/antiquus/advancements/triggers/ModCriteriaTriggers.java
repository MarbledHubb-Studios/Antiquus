package com.marbledhubb.antiquus.advancements.triggers;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.advancements.triggers.custom.ChiseledBlockTrigger;
import com.marbledhubb.antiquus.advancements.triggers.custom.ReconstructedFossilContentTrigger;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, Antiquus.MOD_ID);

    public static final Supplier<ChiseledBlockTrigger> CHISELED_BLOCK =
            TRIGGER_TYPES.register("chiseled_block", ChiseledBlockTrigger::new);
    public static final Supplier<ReconstructedFossilContentTrigger> RECONSTRUCTED_FOSSIL_CONTENT =
            TRIGGER_TYPES.register("reconstructed_fossil_content", ReconstructedFossilContentTrigger::new);

    public static void register(IEventBus modEventBus) {
        TRIGGER_TYPES.register(modEventBus);
    }
}
