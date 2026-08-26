package com.marbledhubb.antiquus.network.payload;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.world.level.block.custom.ChiselableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public record ChiselBlockCompletePayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ChiselBlockCompletePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "chisel_block_complete"));

    public static final StreamCodec<FriendlyByteBuf, ChiselBlockCompletePayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    ChiselBlockCompletePayload::pos,
                    ChiselBlockCompletePayload::new
            );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChiselBlockCompletePayload payload, IPayloadContext context) {
        Level level = Minecraft.getInstance().level;
        BlockState state = level.getBlockState(payload.pos);

        if (state.getBlock() instanceof ChiselableBlock chiselableBlock) {
            level.playLocalSound(payload.pos, chiselableBlock.getChiselCompletedSound(), SoundSource.PLAYERS, 1, 1, false);
        }

        level.addDestroyBlockEffect(payload.pos, state);
    }
}
