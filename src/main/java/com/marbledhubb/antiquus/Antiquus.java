package com.marbledhubb.antiquus;

import com.marbledhubb.antiquus.init.ModBlocks;
import com.marbledhubb.antiquus.init.ModItems;
import com.marbledhubb.antiquus.init.ModParticles;
import com.marbledhubb.antiquus.init.ModTabs;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Antiquus.MODID)
public class Antiquus {
    public static final String MODID = "antiquus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Antiquus(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);


        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModTabs.register(modEventBus);
        ModParticles.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
