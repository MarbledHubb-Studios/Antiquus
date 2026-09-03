package com.marbledhubb.antiquus.mixin;

import com.marbledhubb.antiquus.AntiquusClient;
import com.marbledhubb.antiquus.world.level.saveddata.BiomeOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(BiomeManager.class)
public abstract class BiomeManagerMixin {
    @Inject(method = "getNoiseBiomeAtQuart", at = @At("HEAD"), cancellable = true)
    private void injectToGetNoiseBiomeAtQuart(int quartX, int quartY, int quartZ, CallbackInfoReturnable<Holder<Biome>> cir) {
        if (FMLEnvironment.getDist().isDedicatedServer()) return;

        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        Map<BlockPos, BiomeOverride> biomeOverrides = AntiquusClient.biomeOverrides.get(level.dimension());
        if (biomeOverrides == null) return;
        BlockPos pos = new BlockPos(quartX * 4, quartY * 4, quartZ * 4);

        for (BiomeOverride biomeOverride : biomeOverrides.values()) {
            if (new BoundingBox(
                    biomeOverride.from().getX(), biomeOverride.from().getY(), biomeOverride.from().getZ(),
                    biomeOverride.to().getX(), biomeOverride.to().getY(), biomeOverride.to().getZ()
            ).isInside(pos))
                cir.setReturnValue(biomeOverride.biome());
        }
    }
}
