package com.marbledhubb.antiquus.mixin;

import com.marbledhubb.antiquus.init.blocks.PrototaxiteStemBlock;
import com.marbledhubb.antiquus.init.network.BonemealFacePayload;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class BoneMealItemMixin {
    @Inject(method =  "useOn", at = @At("HEAD"))
    private void injectToUseOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        if (level.isClientSide() || !(level.getBlockState(context.getClickedPos()).getBlock() instanceof PrototaxiteStemBlock)) return;

        Direction face = context.getClickedFace();

        PrototaxiteStemBlock.BONEMEALED_FACE.set(face);
        PacketDistributor.sendToAllPlayers(new BonemealFacePayload(face));
    }
}
