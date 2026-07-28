package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.init.blocks.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Antiquus.MODID);

    public static final DeferredBlock<Block> ANCIENT_MOSS_BLOCK = registerBlockWithItem("ancient_moss_block", properties -> new Block(properties.sound(SoundType.MOSS).strength(0.1f).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<AncientMossyCarpetBlock> ANCIENT_MOSS_CARPET = registerBlockWithItem("ancient_moss_carpet", properties -> new AncientMossyCarpetBlock(properties.sound(SoundType.MOSS_CARPET).strength(0.1f).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> ANCIENT_SOIL = registerBlockWithItem("ancient_soil", properties -> new Block(properties.sound(SoundType.ROOTED_DIRT).strength(2f)));
    public static final DeferredBlock<Block> COOKSONIA = registerBlockWithItem("cooksonia", properties -> new FlowerBlock(SuspiciousStewEffects.EMPTY,properties.noCollision().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ).instabreak().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> POTTED_COOKSONIA = registerBlockWithItem("potted_cooksonia", properties -> new FlowerPotBlock(COOKSONIA.get(),properties.sound(SoundType.STONE).strength(2f).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<Block> PROTOTAXITE_SPORES = registerBlockWithItem("prototaxite_spores", properties -> new PrototaxiteSporesBlock(properties.noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> PROTOTAXITE_BUD = registerBlockWithItem("prototaxite_bud", properties -> new PrototaxiteBudBlock(properties.noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<PrototaxiteStemBlock> PROTOTAXITE_STEM = registerBlockWithItem("prototaxite_stem", properties -> new PrototaxiteStemBlock(properties.randomTicks().sound(SoundType.FUNGUS).strength(2f)));
    public static final DeferredBlock<Block> PROTOTAXITE_BLOCK = registerBlockWithItem("prototaxite_block", properties -> new Block(properties.sound(SoundType.FUNGUS).strength(2f)));
    public static final DeferredBlock<Block> POTTED_PROTOTAXITE_BUD = registerBlockWithItem("potted_prototaxite_bud", properties -> new FlowerPotBlock(PROTOTAXITE_BUD.get(), properties.sound(SoundType.STONE).strength(2f).pushReaction(PushReaction.DESTROY)));

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
        DeferredBlock<B> toReturn = BLOCKS.registerBlock(name, func);
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(toReturn.get(), properties.useBlockDescriptionPrefix()));
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
