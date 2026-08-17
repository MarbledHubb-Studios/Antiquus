package com.marbledhubb.antiquus.level.entity.ai.attributes;

import com.marbledhubb.antiquus.level.entity.ModEntityTypes;
import com.marbledhubb.antiquus.level.entity.custom.animal.trigonotarbid.Trigonotarbid;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class ModDefaultAttributes {
    public static void register(EntityAttributeCreationEvent event) {
        event.put(
                ModEntityTypes.TRIGONOTARBID.get(),
                Trigonotarbid.createAttributes().build()
        );
    }
}
