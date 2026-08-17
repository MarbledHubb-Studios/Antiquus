package com.marbledhubb.antiquus.mixin;

import com.marbledhubb.antiquus.AntiquusClient;
import com.marbledhubb.antiquus.level.saved_data.BiomeOverride;
import com.marbledhubb.antiquus.level.saved_data.BiomeOverrides;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LevelReader.class)
public interface LevelReaderMixin {
    @Inject(method = "getBiome", at = @At("HEAD"), cancellable = true)
    private void injectToGetBiome(BlockPos pos, CallbackInfoReturnable<Holder<Biome>> cir) {
        LevelReader self = (LevelReader) (Object) this;

        Map<BlockPos, BiomeOverride> biomeOverrides;
        if (self instanceof ServerLevel level) {
            biomeOverrides = BiomeOverrides.get(level);
        } else if (self instanceof Level level) {
            biomeOverrides = AntiquusClient.biomeOverrides.get(level.dimension());
            if (biomeOverrides == null) return;
        } else {
            return;
        }

        for (BiomeOverride biomeOverride : biomeOverrides.values()) {
            if (new BoundingBox(
                    biomeOverride.from().getX(), biomeOverride.from().getY(), biomeOverride.from().getZ(),
                    biomeOverride.to().getX(), biomeOverride.to().getY(), biomeOverride.to().getZ()
            ).isInside(pos))
                cir.setReturnValue(biomeOverride.biome());
        }
    }
}
