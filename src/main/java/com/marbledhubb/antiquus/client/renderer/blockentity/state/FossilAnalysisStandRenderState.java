package com.marbledhubb.antiquus.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class FossilAnalysisStandRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState itemState = new ItemStackRenderState();
    public Direction facing;
}
