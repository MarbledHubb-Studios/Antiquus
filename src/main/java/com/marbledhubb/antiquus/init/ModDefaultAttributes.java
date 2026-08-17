package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.init.entities.Trigonotarbid;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class ModDefaultAttributes {
    public static void register(EntityAttributeCreationEvent event) {
        event.put(
                ModEntityTypes.TRIGONOTARBID.get(),
                Trigonotarbid.createAttributes().build()
        );
    }
}
