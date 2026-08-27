package com.marbledhubb.antiquus.client.network;

import com.marbledhubb.antiquus.network.payload.BiomeOverridesPayload;
import com.marbledhubb.antiquus.network.payload.PrototaxiteStemBonemealFacePayload;
import com.marbledhubb.antiquus.network.payload.ChiselBlockCompletePayload;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

public class ModClientNetworking {
    public static void register(RegisterClientPayloadHandlersEvent event) {
        event.register(
                BiomeOverridesPayload.TYPE,
                BiomeOverridesPayload::handle
        );
        event.register(
                PrototaxiteStemBonemealFacePayload.TYPE,
                PrototaxiteStemBonemealFacePayload::handle
        );
        event.register(
                ChiselBlockCompletePayload.TYPE,
                ChiselBlockCompletePayload::handle
        );
    }
}
