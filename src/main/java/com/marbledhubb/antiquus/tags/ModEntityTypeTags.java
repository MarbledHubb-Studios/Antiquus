package com.marbledhubb.antiquus.tags;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModEntityTypeTags {
    TagKey<EntityType<?>> TRIGONOTARBID_HOSTILES = create("trigonotarbid_hostiles");

    private static TagKey<EntityType<?>> create(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, name));
    }
}
