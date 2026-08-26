package com.marbledhubb.antiquus.world.level.block;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.data.worldgen.features.ModConfiguredFeatures;
import com.marbledhubb.antiquus.world.item.ModItems;
import com.marbledhubb.antiquus.world.level.block.custom.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Antiquus.MOD_ID);

    public static final DeferredBlock<Block> ANCIENT_MOSS_BLOCK = registerBlockWithItem("ancient_moss_block", properties -> new BonemealableFeaturePlacerBlock(ModConfiguredFeatures.ANCIENT_MOSS_PATCH_BONEMEAL, properties.ignitedByLava().sound(SoundType.MOSS).strength(0.1f).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<AncientMossyCarpetBlock> ANCIENT_MOSS_CARPET = registerBlockWithItem("ancient_moss_carpet", properties -> new AncientMossyCarpetBlock(properties.ignitedByLava().sound(SoundType.MOSS_CARPET).strength(0.1f).pushReaction(PushReaction.DESTROY).noOcclusion()));
    public static final DeferredBlock<Block> ANCIENT_SOIL = registerBlockWithItem("ancient_soil", properties -> new Block(properties.sound(SoundType.ROOTED_DIRT).strength(2f)));
    public static final DeferredBlock<SandBlock> ANCIENT_SAND = registerBlockWithItem("ancient_sand", properties -> new SandBlock(new ColorRGBA(182159961), properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
    public static final DeferredBlock<Block> ANCIENT_SANDSTONE = registerBlockWithItem("ancient_sandstone", properties -> new Block(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<StairBlock> ANCIENT_SANDSTONE_STAIRS = registerBlockWithItem("ancient_sandstone_stairs", properties -> new StairBlock(ModBlocks.ANCIENT_SANDSTONE.get().defaultBlockState(), properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<SlabBlock> ANCIENT_SANDSTONE_SLAB = registerBlockWithItem("ancient_sandstone_slab", properties -> new SlabBlock(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<WallBlock> ANCIENT_SANDSTONE_WALL = registerBlockWithItem("ancient_sandstone_wall", properties -> new WallBlock(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<Block> CHISELED_ANCIENT_SANDSTONE = registerBlockWithItem("chiseled_ancient_sandstone", properties -> new Block(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<Block> SMOOTH_ANCIENT_SANDSTONE = registerBlockWithItem("smooth_ancient_sandstone", properties -> new Block(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<StairBlock> SMOOTH_ANCIENT_SANDSTONE_STAIRS = registerBlockWithItem("smooth_ancient_sandstone_stairs", properties -> new StairBlock(ModBlocks.SMOOTH_ANCIENT_SANDSTONE.get().defaultBlockState(), properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<SlabBlock> SMOOTH_ANCIENT_SANDSTONE_SLAB = registerBlockWithItem("smooth_ancient_sandstone_slab", properties -> new SlabBlock(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<Block> CUT_ANCIENT_SANDSTONE = registerBlockWithItem("cut_ancient_sandstone", properties -> new Block(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<SlabBlock> CUT_ANCIENT_SANDSTONE_SLAB = registerBlockWithItem("cut_ancient_sandstone_slab", properties -> new SlabBlock(properties.mapColor(MapColor.SAND).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static final DeferredBlock<Block> COOKSONIA = registerBlockWithItem("cooksonia", properties -> new FlowerBlock(SuspiciousStewEffects.EMPTY,properties.noCollision().sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).instabreak().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> POTTED_COOKSONIA = registerBlockWithItem("potted_cooksonia", properties -> new FlowerPotBlock(COOKSONIA.get(),properties.sound(SoundType.STONE).strength(2f).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> ZOSTEROPHYLLUM = registerBlockWithItem("zosterophyllum", properties -> new FlowerBlock(SuspiciousStewEffects.EMPTY, properties.instabreak().noCollision().pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ).sound(SoundType.WET_GRASS)));
    public static final DeferredBlock<Block> SPOROGONITES = registerBlockWithItem("sporogonites", properties -> new FlowerBlock(SuspiciousStewEffects.EMPTY, properties.instabreak().noCollision().pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ).sound(SoundType.WET_GRASS)));

    public static final DeferredBlock<Block> PROTOTAXITE_SPORES = registerBlockWithItem("prototaxite_spores", properties -> new PrototaxiteSporesBlock(properties.noCollision().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> PROTOTAXITE_BUD = registerBlockWithItem("prototaxite_bud", properties -> new PrototaxiteBudBlock(properties.noCollision().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<PrototaxiteStemBlock> PROTOTAXITE_STEM = registerBlockWithItem("prototaxite_stem", properties -> new PrototaxiteStemBlock(properties.randomTicks().sound(SoundType.FUNGUS).strength(2f)));
    public static final DeferredBlock<Block> PROTOTAXITE_BLOCK = registerBlockWithItem("prototaxite_block", properties -> new Block(properties.sound(SoundType.FUNGUS).strength(2f)));
    public static final DeferredBlock<Block> POTTED_PROTOTAXITE_BUD = registerBlockWithItem("potted_prototaxite_bud", properties -> new FlowerPotBlock(PROTOTAXITE_BUD.get(), properties.sound(SoundType.STONE).strength(2f).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<ChiselableBlock> SUSPICIOUS_STONE = registerBlockWithItem("suspicious_stone", properties -> new ChiselableBlock(
            Blocks.STONE,
            SoundType.STONE.getStepSound(),
            SoundType.STONE.getBreakSound(),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "archaeology/suspicious_stone")),
            properties.mapColor(MapColor.STONE).instabreak().pushReaction(PushReaction.DESTROY).noLootTable()));

    public static final DeferredBlock<FossilAnalysisStandBlock> FOSSIL_ANALYSIS_STAND = registerBlockWithItem("fossil_analysis_stand", properties -> new FossilAnalysisStandBlock(properties.strength(2f).sound(SoundType.IRON)));

    public static final DeferredBlock<Block> AGLAOPHYTON_STEM = registerBlockWithItem("aglaophyton_stem", properties -> new Block(properties.strength(2f).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredBlock<Block> AGLAOPHYTON_BLOCK = registerBlockWithItem("aglaophyton_block", properties -> new Block(properties.strength(2f).sound(SoundType.CHERRY_WOOD)));
    public static final DeferredBlock<FlowerBlock> AGLAOPHYTON_SPROUT = registerBlockWithItem("aglaophyton_sprout", properties -> new FlowerBlock(SuspiciousStewEffects.EMPTY, properties.instabreak().noCollision().pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ).sound(SoundType.GRASS)));

    private static <B extends Block> DeferredBlock<B> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
        DeferredBlock<B> toReturn = BLOCKS.registerBlock(name, func);
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(toReturn.get(), properties.useBlockDescriptionPrefix()));
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
