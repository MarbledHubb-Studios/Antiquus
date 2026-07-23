package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.init.blocks.AncientMossyCarpetBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Antiquus.MODID);

    public static final DeferredBlock<AncientMossyCarpetBlock> ANCIENT_MOSS_CARPET = BLOCKS.registerBlock("ancient_moss_carpet", properties -> new AncientMossyCarpetBlock(properties.sound(SoundType.MOSS_CARPET).strength(0.1f).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> ANCIENT_SOIL = BLOCKS.registerBlock("ancient_soil", properties -> new Block(properties.sound(SoundType.MUD).strength(2f)));

    public static final DeferredBlock<Block> PROTOTAXITE_STEM = BLOCKS.registerBlock("prototaxite_stem", properties -> new Block(properties.sound(SoundType.FUNGUS).strength(2f)));
    public static final DeferredBlock<Block> PROTOTAXITE_BUD = BLOCKS.registerBlock("prototaxite_bud", properties -> new Block(properties.sound(SoundType.FUNGUS).strength(0.1f).pushReaction(PushReaction.DESTROY).noOcclusion()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
