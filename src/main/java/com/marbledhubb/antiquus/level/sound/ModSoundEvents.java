package com.marbledhubb.antiquus.level.sound;

import com.marbledhubb.antiquus.Antiquus;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Antiquus.MOD_ID);

    public static final Supplier<SoundEvent> PROTOTAXITE_STEM_CREAKING_AMBIENCE = registerSoundEvents("prototaxite_stem_creaking_ambience");
    public static final Holder<SoundEvent> MUSIC_BIOME_ANCIENT_WETLANDS = registerSoundEvents("music.overworld.ancient_wetlands");

    public static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, name)));
    }

    public static void  register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
