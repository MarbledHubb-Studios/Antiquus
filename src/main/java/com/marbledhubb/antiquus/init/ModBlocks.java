package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.init.blocks.AncientMossyCarpetBlock;
import com.marbledhubb.antiquus.init.blocks.PrototaxiteStemBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Antiquus.MODID);

    public static final DeferredBlock<AncientMossyCarpetBlock> ANCIENT_MOSS_CARPET = registerBlockWithItem("ancient_moss_carpet", properties -> new AncientMossyCarpetBlock(properties.sound(SoundType.MOSS_CARPET).strength(0.1f).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> ANCIENT_SOIL = registerBlockWithItem("ancient_soil", properties -> new Block(properties.sound(SoundType.MUD).strength(2f)));

    public static final DeferredBlock<PrototaxiteStemBlock> PROTOTAXITE_STEM = registerBlockWithItem("prototaxite_stem", properties -> new PrototaxiteStemBlock(properties.randomTicks().sound(SoundType.FUNGUS).strength(2f)));
    public static final DeferredBlock<Block> PROTOTAXITE_BUD = registerBlockWithItem("prototaxite_bud", properties -> new Block(properties.sound(SoundType.FUNGUS).strength(0.1f).pushReaction(PushReaction.DESTROY).noOcclusion()));

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
        DeferredBlock<B> toReturn = BLOCKS.registerBlock(name, func);
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(toReturn.get(), properties.useBlockDescriptionPrefix()));
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
