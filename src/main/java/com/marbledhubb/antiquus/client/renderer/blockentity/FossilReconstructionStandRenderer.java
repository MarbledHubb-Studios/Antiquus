package com.marbledhubb.antiquus.client.renderer.blockentity;

import com.marbledhubb.antiquus.client.renderer.blockentity.state.FossilReconstructionStandRenderState;
import com.marbledhubb.antiquus.world.level.block.custom.FossilReconstructionStandBlock;
import com.marbledhubb.antiquus.world.level.block.entity.custom.FossilReconstructionStandBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FossilReconstructionStandRenderer implements BlockEntityRenderer<FossilReconstructionStandBlockEntity, FossilReconstructionStandRenderState> {
    private final ItemModelResolver itemModelResolver;

    public FossilReconstructionStandRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public @NonNull FossilReconstructionStandRenderState createRenderState() {
        return new FossilReconstructionStandRenderState();
    }

    @Override
    public void extractRenderState(
            @NonNull FossilReconstructionStandBlockEntity blockEntity,
            @NonNull FossilReconstructionStandRenderState state,
            float partialTicks,
            @NonNull Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        ItemStack stack = blockEntity.getItem(FossilReconstructionStandBlockEntity.FOSSIL_SLOT);
        if (stack.isEmpty()) stack = blockEntity.getItem(FossilReconstructionStandBlockEntity.RESULT_SLOT);
        this.itemModelResolver.updateForTopItem(state.itemState, stack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        state.facing = blockEntity.getBlockState().getValue(FossilReconstructionStandBlock.FACING);
    }

    @Override
    public void submit(FossilReconstructionStandRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        if (state.itemState.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(
                0.5f,
                0.140625f,
                0.5f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot() + 180));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        state.itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
