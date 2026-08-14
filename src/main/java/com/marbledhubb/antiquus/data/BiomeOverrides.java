package com.marbledhubb.antiquus.data;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.init.network.BiomeOverridesPayload;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeOverrides extends SavedData {
    private static final Codec<BlockPos> BLOCK_POS_KEY_CODEC =
            Codec.STRING.xmap(
                    string -> {
                        String[] parts = string.split(",");
                        return new BlockPos(
                                Integer.parseInt(parts[0]),
                                Integer.parseInt(parts[1]),
                                Integer.parseInt(parts[2])
                        );
                    },
                    pos -> pos.getX() + "," + pos.getY() + "," + pos.getZ()
            );
    private static final Codec<BiomeOverrides> CODEC = Codec.unboundedMap(BLOCK_POS_KEY_CODEC, BiomeOverride.CODEC)
            .xmap(
                    map -> {
                        BiomeOverrides data = new BiomeOverrides();
                        data.biomeOverrides.putAll(map);
                        return data;
                    },
                    data -> data.biomeOverrides
            );
    private static final SavedDataType<BiomeOverrides> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "biome_overrides"),
            BiomeOverrides::new,
            CODEC
    );
    private final Map<BlockPos, BiomeOverride> biomeOverrides = new HashMap<>();

    public static Map<BlockPos, BiomeOverride> get(ServerLevel level) {
        return getData(level).biomeOverrides;
    }

    public static void add(ServerLevel level, BlockPos origin, Holder<Biome> biome, BlockPos from, BlockPos to) {
        BiomeOverrides data = getData(level);

        data.biomeOverrides.put(origin.immutable(), new BiomeOverride(biome, from, to));
        data.setDirty();
        PacketDistributor.sendToAllPlayers(new BiomeOverridesPayload(Map.of(level.dimension(), data.biomeOverrides)));
        resendBiomesForChunks(level, from, to);
    }

    public static void remove(ServerLevel level, BlockPos origin) {
        BiomeOverrides data = getData(level);

        BiomeOverride biomeOverride = data.biomeOverrides.remove(origin);
        if (biomeOverride != null) {
            data.setDirty();
            PacketDistributor.sendToAllPlayers(new BiomeOverridesPayload(Map.of(level.dimension(), data.biomeOverrides)));
            resendBiomesForChunks(level, biomeOverride.from(), biomeOverride.to());
        }
    }

    private static BiomeOverrides getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    private static void resendBiomesForChunks(ServerLevel level, BlockPos from, BlockPos to) {
        List<ChunkAccess> chunks = new ArrayList<>();

        for(int chunkZ = SectionPos.blockToSectionCoord(from.getZ()); chunkZ <= SectionPos.blockToSectionCoord(to.getZ()); ++chunkZ) {
            for(int chunkX = SectionPos.blockToSectionCoord(from.getX()); chunkX <= SectionPos.blockToSectionCoord(to.getX()); ++chunkX) {
                ChunkAccess chunk = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);

                chunks.add(chunk);
            }
        }

        level.getChunkSource().chunkMap.resendBiomesForChunks(chunks);
    }

    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        Map<ResourceKey<Level>, Map<BlockPos, BiomeOverride>> biomeOverrides = new HashMap<>();
        for (ServerLevel level : player.level().getServer().getAllLevels()) {
            Map<BlockPos, BiomeOverride> levelBiomeOverrides = getData(level).biomeOverrides;
            if (!levelBiomeOverrides.isEmpty()) biomeOverrides.put(level.dimension(), levelBiomeOverrides);
        }

        if (biomeOverrides.isEmpty()) return;

        PacketDistributor.sendToPlayer(player, new BiomeOverridesPayload(biomeOverrides));
    }
}
