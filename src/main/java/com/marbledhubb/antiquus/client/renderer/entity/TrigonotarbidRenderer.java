package com.marbledhubb.antiquus.client.renderer.entity;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.client.model.custom.animal.TrigonotarbidModel;
import com.marbledhubb.antiquus.client.renderer.entity.state.TrigonotarbidRenderState;
import com.marbledhubb.antiquus.client.model.ModModelLayers;
import com.marbledhubb.antiquus.world.entity.custom.animal.trigonotarbid.Trigonotarbid;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class TrigonotarbidRenderer<T extends Trigonotarbid> extends MobRenderer<T, TrigonotarbidRenderState, TrigonotarbidModel> {
    private static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "textures/entity/trigonotarbid/trigonotarbid.png");

    public TrigonotarbidRenderer(EntityRendererProvider.Context context) {
        super(context, new TrigonotarbidModel(context.bakeLayer(ModModelLayers.TRIGONOTARBID)), 0.6f);
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull TrigonotarbidRenderState state) {
        return TEXTURE_LOCATION;
    }

    @Override
    public @NonNull TrigonotarbidRenderState createRenderState() {
        return new TrigonotarbidRenderState();
    }
}
