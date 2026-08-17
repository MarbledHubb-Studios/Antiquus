package com.marbledhubb.antiquus.network;

import com.marbledhubb.antiquus.network.payload.BiomeOverridesPayload;
import com.marbledhubb.antiquus.network.payload.BonemealFacePayload;
import com.marbledhubb.antiquus.network.payload.ChiselBlockCompletePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworking {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                BiomeOverridesPayload.TYPE,
                BiomeOverridesPayload.STREAM_CODEC
        );
        registrar.playToClient(
                BonemealFacePayload.TYPE,
                BonemealFacePayload.STREAM_CODEC
        );
        registrar.playToClient(
                ChiselBlockCompletePayload.TYPE,
                ChiselBlockCompletePayload.STREAM_CODEC
        );
    }
}
