package com.marbledhubb.antiquus;

import com.marbledhubb.antiquus.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;
import org.slf4j.Logger;

@Mod(Antiquus.MODID)
public class Antiquus {
    public static final String MODID = "antiquus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Antiquus(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerCauldronInteractions);
        modEventBus.addListener(ModNetworking::register);
        //NeoForge.EVENT_BUS.register(this);


        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModTabs.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.register(modEventBus);
    }

    private void registerCauldronInteractions(RegisterCauldronInteractionEvent.Interaction event) {
        registerFossilizedItemCauldronIteration(event, ModItems.FOSSILIZED_PROTOTAXITE_SPORES.get(), ModBlocks.PROTOTAXITE_SPORES.asItem());
        registerFossilizedItemCauldronIteration(event, ModItems.FOSSILIZED_PROTOTAXITE_BUD.get(), ModBlocks.PROTOTAXITE_BUD.asItem());
        registerFossilizedItemCauldronIteration(event, ModItems.FOSSILIZED_COOKSONIA.get(), ModBlocks.COOKSONIA.asItem());
    }

    private static void registerFossilizedItemCauldronIteration(RegisterCauldronInteractionEvent.Interaction event, Item interactionItem, Item returnItem) {
        event.register(
                Identifier.withDefaultNamespace("water"),
                interactionItem,
                (state, level, pos, player, hand, stackInHand) ->
                {
                    if (!level.isClientSide()) {
                        player.setItemInHand(hand, ItemUtils.createFilledResult(stackInHand, player, returnItem.getDefaultInstance()));
                        player.awardStat(Stats.USE_CAULDRON);
                        LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1f, 1f);
                        level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
                    }

                    return InteractionResult.SUCCESS;
                }
        );
    }
}
