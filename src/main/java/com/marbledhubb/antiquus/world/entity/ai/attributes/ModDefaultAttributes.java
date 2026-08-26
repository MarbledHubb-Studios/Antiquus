package com.marbledhubb.antiquus.world.entity.ai.attributes;

import com.marbledhubb.antiquus.world.entity.ModEntityTypes;
import com.marbledhubb.antiquus.world.entity.custom.animal.trigonotarbid.Trigonotarbid;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class ModDefaultAttributes {
    public static void register(EntityAttributeCreationEvent event) {
        event.put(
                ModEntityTypes.TRIGONOTARBID.get(),
                Trigonotarbid.createAttributes().build()
        );
    }
}
