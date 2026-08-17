package com.marbledhubb.antiquus.level.item.custom;

import com.marbledhubb.antiquus.level.block.ModBlocks;
import com.marbledhubb.antiquus.level.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.marbledhubb.antiquus.Antiquus.MOD_ID;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final Supplier<CreativeModeTab> ANTIQUUS_TAB = CREATIVE_MODE_TABS.register("antiquus_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.%s.antiquus_tab".formatted(MOD_ID)))
            .icon(() -> new ItemStack(ModBlocks.COOKSONIA))
            .displayItems((params, output) -> {
                output.accept(ModBlocks.ANCIENT_SOIL);
                output.accept(ModBlocks.ANCIENT_MOSS_BLOCK);
                output.accept(ModBlocks.ANCIENT_MOSS_CARPET);
                output.accept(ModBlocks.PROTOTAXITE_STEM);
                output.accept(ModBlocks.PROTOTAXITE_BLOCK);
                output.accept(ModBlocks.PROTOTAXITE_BUD);
                output.accept(ModBlocks.PROTOTAXITE_SPORES);
                output.accept(ModBlocks.COOKSONIA);
                output.accept(ModBlocks.ZOSTEROPHYLLUM);
                output.accept(ModBlocks.SPOROGONITES);
                output.accept(ModBlocks.SUSPICIOUS_STONE);
                output.accept(ModItems.FOSSILIZED_PROTOTAXITE_BUD);
                output.accept(ModItems.FOSSILIZED_PROTOTAXITE_SPORES);
                output.accept(ModItems.FOSSILIZED_COOKSONIA);
                output.accept(ModItems.FOSSILIZED_ZOSTEROPHYLLUM);
                output.accept(ModItems.ROCK_HAMMER);
                output.accept(ModItems.ROCK_CHISEL);

            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
