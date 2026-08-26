package com.marbledhubb.antiquus.world.inventory;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.world.inventory.custom.FossilAnalysisStandMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, Antiquus.MOD_ID);

    public static final Supplier<MenuType<FossilAnalysisStandMenu>> FOSSIL_ANALYSIS_STAND = MENU_TYPES.register("fossil_analysis_stand", () -> new MenuType<>(FossilAnalysisStandMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
