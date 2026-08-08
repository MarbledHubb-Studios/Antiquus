package com.marbledhubb.antiquus.init.network;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.init.blocks.PrototaxiteStemBlock;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record BonemealFacePayload(Direction face) implements CustomPacketPayload {
    public static final Type<BonemealFacePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "bonemeal_face"));

    public static final StreamCodec<FriendlyByteBuf, BonemealFacePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.idMapper(Direction::from3DDataValue, Direction::get3DDataValue),
                    BonemealFacePayload::face,
                    BonemealFacePayload::new
            );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BonemealFacePayload payload, IPayloadContext context) {
        PrototaxiteStemBlock.BONEMEALED_FACE.set(payload.face);
    }
}
