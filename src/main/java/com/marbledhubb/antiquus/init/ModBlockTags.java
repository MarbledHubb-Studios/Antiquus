package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> SUPPORTS_PROTOTAXITE = create("supports_prototaxite");
    public static final TagKey<Block> SILURIAN_REPLACEABLE = create("silurian_replaceable");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Antiquus.MODID, name));
    }
}
