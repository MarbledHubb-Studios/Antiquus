package com.marbledhubb.antiquus.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class ModClientEnumParams {
    public static final EnumProxy<HumanoidModel.ArmPose> ROCK_HAMMER = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            false,
            (IArmPoseTransformer) ModClientEnumParams::applyRockHammerPose
    );

    private static void applyRockHammerPose(HumanoidModel<?> model, HumanoidRenderState state, HumanoidArm arm) {
        ModelPart modelArm = model.getArm(arm);

        modelArm.xRot = modelArm.xRot * 0.5f - (float) (Math.PI / 5);
        modelArm.yRot = 0f;
    }
}
