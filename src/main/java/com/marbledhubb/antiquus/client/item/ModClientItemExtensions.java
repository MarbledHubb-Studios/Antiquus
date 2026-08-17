package com.marbledhubb.antiquus.client.item;

import com.marbledhubb.antiquus.client.ModArmPoses;
import com.marbledhubb.antiquus.level.item.custom.ModItemUseAnimations;
import com.marbledhubb.antiquus.level.item.custom.RockHammerItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jspecify.annotations.NonNull;

public class ModClientItemExtensions implements IClientItemExtensions {
    @Override
    public boolean applyForgeHandTransform(@NonNull PoseStack poseStack, @NonNull LocalPlayer player, @NonNull HumanoidArm arm, @NonNull ItemStack stackInHand,
                                           float partialTick, float equipProcess, float swingProcess) {
        InteractionHand usedItemHand = player.getUsedItemHand();
        HumanoidArm usingArm = usedItemHand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0) {
            if (usingArm == arm) {
                if (stackInHand.getUseAnimation() == ModItemUseAnimations.ROCK_HAMMER) {
                    int invert = getInvert(arm);
                    applyItemArmTransform(poseStack, invert, equipProcess);
                    applyRockHammerTransform(poseStack, partialTick, invert, player);
                    return true;
                }
            } else if (player.getItemInHand(usedItemHand).getUseAnimation() == ModItemUseAnimations.ROCK_HAMMER && stackInHand.getUseAnimation() == ModItemUseAnimations.ROCK_CHISEL) {
                int invert = getInvert(arm);
                applyItemArmTransform(poseStack, invert, equipProcess);
                applyRockChiselTransform(poseStack, partialTick, invert, player);
                return true;
            }
        }

        return false;
    }

    private static int getInvert(HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? 1 : -1;
    }

    private static void applyItemArmTransform(PoseStack poseStack, int invert, float equipProcess) {
        poseStack.translate(invert * 0.56f, -0.52f + equipProcess * -0.6f, -0.72f);
    }

    private static void applyRockHammerTransform(PoseStack poseStack, float partialTick, int invert, Player player) {
        float animationRemainingTicks = player.getUseItemRemainingTicks() % RockHammerItem.ANIMATION_DURATION;
        float deltaSinceLastUpdate = animationRemainingTicks - partialTick + 1f;
        float scaledUsageTime = 1f - deltaSinceLastUpdate / RockHammerItem.ANIMATION_DURATION;

        poseStack.translate(invert * -0.5f, 0.15, 0.09);

        float ySwingRotation = Mth.sin(scaledUsageTime * scaledUsageTime * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(invert * (45f + ySwingRotation * -13f)));
        float xzSwingRotation = Mth.sin(Mth.sqrt(scaledUsageTime) * (float) Math.PI);
        poseStack.mulPose(Axis.ZP.rotationDegrees(invert * xzSwingRotation * -13f));
        poseStack.mulPose(Axis.XP.rotationDegrees(xzSwingRotation * -52f));
        poseStack.mulPose(Axis.YP.rotationDegrees(invert * -25f));
    }

    private static void applyRockChiselTransform(PoseStack poseStack, float partialTick, int invert, Player player) {
        float animationRemainingTicks = player.getUseItemRemainingTicks() % RockHammerItem.ANIMATION_DURATION;
        float deltaSinceLastUpdate = animationRemainingTicks - partialTick + 1f;

        poseStack.translate(invert * -0.48f, -0.2f - getChiselBounce(deltaSinceLastUpdate) * 0.03f, -0.25f);
        poseStack.mulPose(Axis.YP.rotationDegrees(invert * 60f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-25f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(invert * 25f));
    }

    private static float getChiselBounce(float deltaSinceLastUpdate) {
        float scaledUsageTime = 1f - deltaSinceLastUpdate / RockHammerItem.ANIMATION_DURATION;

        float impactStart = 0.15f;
        float impactPeak = 0.25f;
        float impactEnd = 0.39f;

        float chiselBounce = 0;

        if (scaledUsageTime >= impactStart && scaledUsageTime <= impactPeak) {
            float progress = (scaledUsageTime - impactStart) / (impactPeak - impactStart);
            chiselBounce = Mth.sin(progress * (float) Math.PI * 0.5f);
        } else if (scaledUsageTime > impactPeak && scaledUsageTime <= impactEnd) {
            float progress = (scaledUsageTime - impactPeak) / (impactEnd - impactPeak);
            chiselBounce = Mth.cos(progress * (float) Math.PI * 0.5f);
        }
        return chiselBounce;
    }

    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entity, @NonNull InteractionHand hand, @NonNull ItemStack stackInHand) {
        if (entity.isUsingItem() && entity.getUseItemRemainingTicks() > 0 && entity.getUsedItemHand() == hand && stackInHand.getUseAnimation() == ModItemUseAnimations.ROCK_HAMMER) {
            if (entity.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND).getUseAnimation() == ModItemUseAnimations.ROCK_CHISEL)
                return ModArmPoses.ROCK_HAMMER_AND_CHISEL;
            return ModArmPoses.ROCK_HAMMER;
        }

        return null;
    }
}
