package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SILURIAN_PATCH = createKey("silurian_patch");

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Antiquus.MODID, name));
    }
}
