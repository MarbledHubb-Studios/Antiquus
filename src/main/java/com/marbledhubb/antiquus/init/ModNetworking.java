package com.marbledhubb.antiquus.init;

import com.marbledhubb.antiquus.init.network.*;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetworking {
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                BonemealFacePayload.TYPE,
                BonemealFacePayload.STREAM_CODEC
        );
    }

    public static void registerClient(RegisterClientPayloadHandlersEvent event) {
        event.register(
                BonemealFacePayload.TYPE,
                BonemealFacePayload::handle
        );
    }
}
