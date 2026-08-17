package com.marbledhubb.antiquus.level.entity.custom.animal.trigonotarbid;

import com.marbledhubb.antiquus.level.entity.ai.sensing.ModSensorTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class Trigonotarbid extends PathfinderMob {
    private static final Brain.Provider<Trigonotarbid> BRAIN_PROVIDER = Brain.provider(List.of(SensorType.NEAREST_LIVING_ENTITIES, /*SensorType.HURT_BY,*/ ModSensorTypes.TRIGONOTARBID_ATTACKABLES.get()), _ -> TrigonotarbidAi.getActivities());

    public Trigonotarbid(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected @NonNull Brain<Trigonotarbid> makeBrain(Brain.@NonNull Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    @Override
    public @NonNull Brain<Trigonotarbid> getBrain() {
        return (Brain<Trigonotarbid>) super.getBrain();
    }

    @Override
    protected void customServerAiStep(@NonNull ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("trigonotarbidBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        profiler.push("trigonotarbidActivityUpdate");
        TrigonotarbidAi.updateActivity(this);
        profiler.pop();
        super.customServerAiStep(level);
    }

    // TODO
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes();
    }
}
