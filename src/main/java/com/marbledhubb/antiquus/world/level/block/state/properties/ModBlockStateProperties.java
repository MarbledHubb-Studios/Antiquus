package com.marbledhubb.antiquus.world.level.block.state.properties;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final IntegerProperty MAX_PROTOTAXITE_STEM_GROWING_HEIGHT = IntegerProperty.create("max_growing_height", 2, 8);
    public static final IntegerProperty CHISELED = IntegerProperty.create("chiseled", 0, 3);
    public static final IntegerProperty WATER_LEVEL = IntegerProperty.create("water_level", 0, 8);
}
