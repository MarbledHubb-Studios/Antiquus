package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.client.model.animal.TrigonotarbidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModModelLayers {
    public static final ModelLayerLocation TRIGONOTARBID = register("trigonotarbid");

    private static ModelLayerLocation register(String model) {
        return register(model, "main");
    }

    private static ModelLayerLocation register(String model, String layer) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, model), layer);
    }



    public static void registerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TRIGONOTARBID, TrigonotarbidModel::createBodyLayer);
    }
}
