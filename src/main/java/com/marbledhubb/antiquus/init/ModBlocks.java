package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Antiquus.MODID);

    //public static final DeferredBlock<Block> PROTOTAXITE_STEM = BLOCKS.registerBlock("prototaxite_stem", properties -> new Block(BlockBehaviour.Properties.of().sound(SoundType.FUNGUS).strength(2f)));

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties,T> function) {
        return BLOCKS.registerBlock(name, function);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
