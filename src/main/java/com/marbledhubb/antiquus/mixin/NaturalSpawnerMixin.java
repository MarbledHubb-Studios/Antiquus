package com.marbledhubb.antiquus.mixin;

import com.marbledhubb.antiquus.AntiquusClient;
import com.marbledhubb.antiquus.world.level.saveddata.BiomeOverride;
import com.marbledhubb.antiquus.world.level.saveddata.BiomeOverrides;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
    @Inject(method = "getRoughBiome", at = @At("HEAD"),  cancellable = true)
    private static void injectToGetRoughBiome(BlockPos pos, ChunkAccess chunk, CallbackInfoReturnable<Biome> cir) {
        Map<BlockPos, BiomeOverride> biomeOverrides;
        if (chunk.getLevel() instanceof ServerLevel level) {
            biomeOverrides = BiomeOverrides.get(level);
        } else {
            biomeOverrides = AntiquusClient.biomeOverrides.get(chunk.getLevel().dimension());
        }

        for (BiomeOverride biomeOverride : biomeOverrides.values()) {
            if (new BoundingBox(
                    biomeOverride.from().getX(), biomeOverride.from().getY(), biomeOverride.from().getZ(),
                    biomeOverride.to().getX(), biomeOverride.to().getY(), biomeOverride.to().getZ()
            ).isInside(pos))
                cir.setReturnValue(biomeOverride.biome().value());
        }
    }
}
