package com.marbledhubb.antiquus.tags;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> FOSSIL_RECONSTRUCTION_MEDIUM = create("fossil_reconstruction_medium");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, name));
    }
}
