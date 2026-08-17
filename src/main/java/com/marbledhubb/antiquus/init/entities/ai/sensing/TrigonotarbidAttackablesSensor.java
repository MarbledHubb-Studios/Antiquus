package com.marbledhubb.antiquus.init.entities.ai.sensing;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import org.jspecify.annotations.NonNull;

public class TrigonotarbidAttackablesSensor extends NearestVisibleLivingEntitySensor {
    public TrigonotarbidAttackablesSensor() {
    }

    @Override
    protected boolean isMatchingEntity(@NonNull ServerLevel level, @NonNull LivingEntity body, @NonNull LivingEntity mob) {
        return true;
    }

    @Override
    protected @NonNull MemoryModuleType<LivingEntity> getMemoryToSet() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }

    //@Override
    //public @NonNull Set<MemoryModuleType<?>> requires() {
    //    return Sets.union(super.requires(), Set.of(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS));
    //}
}
