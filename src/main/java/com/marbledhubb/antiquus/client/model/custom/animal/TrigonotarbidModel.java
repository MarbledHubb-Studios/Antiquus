package com.marbledhubb.antiquus.client.model.custom.animal;

import com.marbledhubb.antiquus.client.renderer.entity.state.TrigonotarbidRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

public class TrigonotarbidModel extends EntityModel<TrigonotarbidRenderState> {
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightMiddleHindLeg;
    private final ModelPart leftMiddleHindLeg;
    private final ModelPart rightMiddleFrontLeg;
    private final ModelPart leftMiddleFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public TrigonotarbidModel(ModelPart root) {
        super(root);
        ModelPart legs = root.getChild("body0").getChild("legs");
        this.rightHindLeg = legs.getChild("right_hind_leg");
        this.leftHindLeg = legs.getChild("left_hind_leg");
        this.rightMiddleHindLeg = legs.getChild("right_middle_hind_leg");
        this.leftMiddleHindLeg = legs.getChild("left_middle_hind_leg");
        this.rightMiddleFrontLeg = legs.getChild("right_middle_front_leg");
        this.leftMiddleFrontLeg = legs.getChild("left_middle_front_leg");
        this.rightFrontLeg = legs.getChild("right_front_leg");
        this.leftFrontLeg = legs.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body0 = root.addOrReplaceChild("body0", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, 3.0F, -6.0F, 6.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition body1 = body0.addOrReplaceChild("body1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 9.0F));
        body1.addOrReplaceChild("body1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, -6.0F, 0.2182F, 0.0F, 0.0F));
        PartDefinition legs = body0.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));
        legs.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -4.0F, 2.0F, 0.0F, 0.7854F, -0.7854F));
        legs.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(20, 25).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.0F, 2.0F, 0.0F, -0.7854F, 0.7854F));
        legs.addOrReplaceChild("right_middle_hind_leg", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -4.0F, 1.0F, 0.0F, 0.3927F, -0.5812F));
        legs.addOrReplaceChild("left_middle_hind_leg", CubeListBuilder.create().texOffs(20, 25).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.0F, 1.0F, 0.0F, -0.3927F, 0.5812F));
        legs.addOrReplaceChild("right_middle_front_leg", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -4.0F, 0.0F, 0.0F, -0.3927F, -0.5812F));
        legs.addOrReplaceChild("left_middle_front_leg", CubeListBuilder.create().texOffs(20, 25).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.0F, 0.0F, 0.0F, 0.3927F, 0.5812F));
        legs.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 25).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -4.0F, -1.0F, 0.0F, -0.7854F, -0.7854F));
        legs.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(20, 25).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -4.0F, -1.0F, 0.0F, 0.7854F, 0.7854F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(@NonNull TrigonotarbidRenderState state) {
        super.setupAnim(state);
        float animationPos = state.walkAnimationPos * 0.6662F;
        float animationSpeed = state.walkAnimationSpeed;
        float swingHind = -(Mth.cos(animationPos * 2.0F + 0.0F) * 0.4F) * animationSpeed;
        float swingMiddleHind = -(Mth.cos(animationPos * 2.0F + (float)Math.PI) * 0.4F) * animationSpeed;
        float swingMiddleFront = -(Mth.cos(animationPos * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * animationSpeed;
        float swingFront = -(Mth.cos(animationPos * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * animationSpeed;
        float stepHind = Math.abs(Mth.sin(animationPos + 0.0F) * 0.4F) * animationSpeed;
        float stepMiddleHind = Math.abs(Mth.sin(animationPos + (float)Math.PI) * 0.4F) * animationSpeed;
        float stepMiddleFrontHind = Math.abs(Mth.sin(animationPos + ((float)Math.PI / 2F)) * 0.4F) * animationSpeed;
        float stepFront = Math.abs(Mth.sin(animationPos + ((float)Math.PI * 1.5F)) * 0.4F) * animationSpeed;
        this.rightHindLeg.yRot += swingHind;
        this.leftHindLeg.yRot -= swingHind;
        this.rightMiddleHindLeg.yRot += swingMiddleHind;
        this.leftMiddleHindLeg.yRot -= swingMiddleHind;
        this.rightMiddleFrontLeg.yRot += swingMiddleFront;
        this.leftMiddleFrontLeg.yRot -= swingMiddleFront;
        this.rightFrontLeg.yRot += swingFront;
        this.leftFrontLeg.yRot -= swingFront;
        this.rightHindLeg.zRot += stepHind;
        this.leftHindLeg.zRot -= stepHind;
        this.rightMiddleHindLeg.zRot += stepMiddleHind;
        this.leftMiddleHindLeg.zRot -= stepMiddleHind;
        this.rightMiddleFrontLeg.zRot += stepMiddleFrontHind;
        this.leftMiddleFrontLeg.zRot -= stepMiddleFrontHind;
        this.rightFrontLeg.zRot += stepFront;
        this.leftFrontLeg.zRot -= stepFront;
    }
}
