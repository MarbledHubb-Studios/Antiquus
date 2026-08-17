package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.init.entities.ai.sensing.TrigonotarbidAttackablesSensor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES =
            DeferredRegister.create(Registries.SENSOR_TYPE, Antiquus.MOD_ID);

    public static final Supplier<SensorType<TrigonotarbidAttackablesSensor>> TRIGONOTARBID_ATTACKABLES = SENSOR_TYPES.register("trigonotarbid_attackables", () -> new SensorType<>(TrigonotarbidAttackablesSensor::new));

    public static void register(IEventBus eventBus) {
        SENSOR_TYPES.register(eventBus);
    }
}
