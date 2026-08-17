package com.marbledhubb.antiquus.level.entity;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.level.entity.custom.animal.trigonotarbid.Trigonotarbid;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntityTypes {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(Antiquus.MOD_ID);

    public static final Supplier<EntityType<Trigonotarbid>> TRIGONOTARBID = ENTITY_TYPES.registerEntityType(
            "trigonotarbid", Trigonotarbid::new, MobCategory.CREATURE,
            builder -> builder.sized(1, 1).spawnDimensionsScale(4).eyeHeight(0.5f).clientTrackingRange(8));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
