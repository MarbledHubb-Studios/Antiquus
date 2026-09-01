package com.marbledhubb.antiquus.network.payload;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.world.level.block.custom.PrototaxiteStemBlock;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record PrototaxiteStemBonemealFacePayload(Direction face) implements CustomPacketPayload {
    public static final Type<PrototaxiteStemBonemealFacePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "prototaxite_stem_bonemeal_face"));

    public static final StreamCodec<FriendlyByteBuf, PrototaxiteStemBonemealFacePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.idMapper(Direction::from3DDataValue, Direction::get3DDataValue),
                    PrototaxiteStemBonemealFacePayload::face,
                    PrototaxiteStemBonemealFacePayload::new
            );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PrototaxiteStemBonemealFacePayload payload, IPayloadContext context) {
        PrototaxiteStemBlock.BONEMEALED_FACE.set(payload.face);
    }
}
