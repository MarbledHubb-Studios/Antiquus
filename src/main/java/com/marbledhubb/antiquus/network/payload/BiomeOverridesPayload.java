package com.marbledhubb.antiquus.network.payload;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.AntiquusClient;
import com.marbledhubb.antiquus.world.level.saveddata.BiomeOverride;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

public record BiomeOverridesPayload(Map<ResourceKey<Level>, Map<BlockPos, BiomeOverride>> biomeOverrides) implements CustomPacketPayload {
    public static final Type<BiomeOverridesPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "biome_overrides"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BiomeOverridesPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.map(
                            HashMap::new,
                        ResourceKey.streamCodec(Registries.DIMENSION),

                        ByteBufCodecs.map(
                            HashMap::new,
                            BlockPos.STREAM_CODEC,
                            BiomeOverride.STREAM_CODEC
                        )
                    ),
                    BiomeOverridesPayload::biomeOverrides,
                    BiomeOverridesPayload::new
            );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BiomeOverridesPayload payload, IPayloadContext context) {
        AntiquusClient.biomeOverrides.putAll(payload.biomeOverrides);
    }
}
