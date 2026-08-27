package com.marbledhubb.antiquus;

import com.marbledhubb.antiquus.stats.ModStats;
import com.marbledhubb.antiquus.world.inventory.ModMenuTypes;
import com.marbledhubb.antiquus.world.item.crafting.ModRecipeBookCategories;
import com.marbledhubb.antiquus.world.item.crafting.ModRecipeSerializers;
import com.marbledhubb.antiquus.world.item.crafting.ModRecipeTypes;
import com.marbledhubb.antiquus.world.level.block.ModBlocks;
import com.marbledhubb.antiquus.world.particle.ModParticleTypes;
import com.marbledhubb.antiquus.world.level.saveddata.BiomeOverrides;
import com.marbledhubb.antiquus.network.ModNetworking;
import com.marbledhubb.antiquus.world.sound.ModSoundEvents;
import com.marbledhubb.antiquus.world.level.block.entity.ModBlockEntityTypes;
import com.marbledhubb.antiquus.world.entity.ModEntityTypes;
import com.marbledhubb.antiquus.world.entity.ai.attributes.ModDefaultAttributes;
import com.marbledhubb.antiquus.world.entity.ai.sensing.ModSensorTypes;
import com.marbledhubb.antiquus.world.item.ModItems;
import com.marbledhubb.antiquus.world.item.custom.ModCreativeModeTabs;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;
import org.slf4j.Logger;

@Mod(Antiquus.MOD_ID)
public class Antiquus {
    public static final String MOD_ID = "antiquus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Antiquus(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerCauldronInteractions);
        modEventBus.addListener(ModNetworking::register);
        modEventBus.addListener(ModDefaultAttributes::register);
        NeoForge.EVENT_BUS.addListener(BiomeOverrides::onPlayerJoin);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntityTypes.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModSensorTypes.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModParticleTypes.register(modEventBus);
        ModSoundEvents.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModRecipeBookCategories.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModStats.register(modEventBus);
    }

    private void registerCauldronInteractions(RegisterCauldronInteractionEvent.Interaction event) {
        registerFossilizedItemCauldronIteration(event, ModItems.FOSSILIZED_PROTOTAXITE_SPORES.get(), ModBlocks.PROTOTAXITE_SPORES.asItem());
        registerFossilizedItemCauldronIteration(event, ModItems.FOSSILIZED_PROTOTAXITE_BUD.get(), ModBlocks.PROTOTAXITE_BUD.asItem());
        registerFossilizedItemCauldronIteration(event, ModItems.FOSSILIZED_COOKSONIA.get(), ModBlocks.COOKSONIA.asItem());
        registerFossilizedItemCauldronIteration(event, ModItems.FOSSILIZED_ZOSTEROPHYLLUM.get(), ModBlocks.ZOSTEROPHYLLUM.asItem());
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
