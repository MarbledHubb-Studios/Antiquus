package com.marbledhubb.antiquus.client;

import com.marbledhubb.antiquus.level.item.custom.RockHammerItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class ModClientEnumParams {
    public static final EnumProxy<HumanoidModel.ArmPose> ROCK_HAMMER = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            false,
            (IArmPoseTransformer) ModClientEnumParams::applyRockHammerSinglePose
    );
    public static final EnumProxy<HumanoidModel.ArmPose> ROCK_HAMMER_AND_CHISEL = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            true,
            true,
            (IArmPoseTransformer) ModClientEnumParams::applyRockHammerAndChiselPose
    );

    private static void applyRockHammerSinglePose(HumanoidModel<?> model, HumanoidRenderState state, HumanoidArm arm) {
        applyRockHammerPose(model, getInvert(arm), state, arm);
    }

    private static void applyRockHammerAndChiselPose(HumanoidModel<?> model, HumanoidRenderState state, HumanoidArm arm) {
        int invert = getInvert(arm);
        float headPitchCompensation = -(model.head.xRot - 0.7F) * 0.4f;

        applyRockHammerPose(model, invert, state, arm);

        ModelPart armModel = model.getArm(arm.getOpposite());
        armModel.xRot -= headPitchCompensation + 0.7f;
        armModel.yRot += 0.45f * invert;
        //armModel.zRot -= (Mth.sin(swingTime * (float) Math.PI) * 0.1f) * invert;
    }

    private static int getInvert(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? 1 : -1;
    }

    private static void applyRockHammerPose(HumanoidModel<?> model, int invert, HumanoidRenderState state, HumanoidArm arm) {
        float swingTime = (state.ticksUsingItem - 1.5f) % RockHammerItem.ANIMATION_DURATION / RockHammerItem.ANIMATION_DURATION;
        float swingRotation = Mth.sin(Ease.outQuart(swingTime) * (float) Math.PI);
        float headPitchCompensation = Mth.sin(swingTime * (float) Math.PI) * -(model.head.xRot - 0.7F) * 0.4f;
        ModelPart armModel = model.getArm(arm);
        armModel.xRot -= swingRotation * 0.2f + headPitchCompensation + 0.8f;
        armModel.yRot -= 0.45f * invert;
        armModel.zRot -= (Mth.sin(swingTime * (float) Math.PI) * 0.1f) * invert;
    }
}
