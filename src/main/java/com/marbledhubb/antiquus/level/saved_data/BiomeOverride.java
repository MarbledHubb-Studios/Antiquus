package com.marbledhubb.antiquus.level.saved_data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.biome.Biome;

public record BiomeOverride(Holder<Biome> biome, BlockPos from, BlockPos to) {
    public static final Codec<BiomeOverride> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Biome.CODEC.fieldOf("biome").forGetter(BiomeOverride::biome),
                    BlockPos.CODEC.fieldOf("from").forGetter(BiomeOverride::from),
                    BlockPos.CODEC.fieldOf("to").forGetter(BiomeOverride::to)
            ).apply(instance, BiomeOverride::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BiomeOverride> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
}
