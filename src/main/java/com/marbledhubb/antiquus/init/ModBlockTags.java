package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import javax.swing.text.html.HTML;

public class ModBlockTags {
    public static final TagKey<Block> SUPPORTS_PROTOTAXITE = create("supports_prototaxite");
    public static final TagKey<Block> SILURIAN_REPLACEABLE = create("silurian_replaceable");
    public static final TagKey<Block> TRIGGERS_AMBIENT_PROTOTAXITE_STEM_SOUNDS = create("triggers_ambient_prototaxite_stem_sounds");

    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Antiquus.MODID, name));
    }
}
