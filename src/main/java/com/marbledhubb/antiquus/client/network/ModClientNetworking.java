package com.marbledhubb.antiquus.client.network;

import com.marbledhubb.antiquus.network.payload.BiomeOverridesPayload;
import com.marbledhubb.antiquus.network.payload.BonemealFacePayload;
import com.marbledhubb.antiquus.network.payload.ChiselBlockCompletePayload;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

public class ModClientNetworking {
    public static void register(RegisterClientPayloadHandlersEvent event) {
        event.register(
                BiomeOverridesPayload.TYPE,
                BiomeOverridesPayload::handle
        );
        event.register(
                BonemealFacePayload.TYPE,
                BonemealFacePayload::handle
        );
        event.register(
                ChiselBlockCompletePayload.TYPE,
                ChiselBlockCompletePayload::handle
        );
    }
}
