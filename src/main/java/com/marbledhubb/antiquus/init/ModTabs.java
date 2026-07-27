package com.marbledhubb.antiquus.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.marbledhubb.antiquus.Antiquus.MODID;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<CreativeModeTab> ANTIQUUS_TAB = CREATIVE_MODE_TABS.register("antiquus_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID + ".antiquus_tab"))
            .icon(() -> new ItemStack(ModBlocks.COOKSONIA))
            .displayItems((params, output) -> {
                output.accept(ModBlocks.PROTOTAXITE_STEM);
                output.accept(ModBlocks.PROTOTAXITE_BLOCK);
                output.accept(ModBlocks.PROTOTAXITE_BUD);
                output.accept(ModBlocks.PROTOTAXITE_SPORES);
                output.accept(ModBlocks.ANCIENT_SOIL);
                output.accept(ModBlocks.ANCIENT_MOSS_BLOCK);
                output.accept(ModBlocks.ANCIENT_MOSS_CARPET);
                output.accept(ModBlocks.COOKSONIA);
                output.accept(ModItems.FOSSILIZED_PROTOTAXITE_SPORES);
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
