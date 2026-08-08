package com.marbledhubb.antiquus.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

public class ChiselableBlockRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState itemState = new ItemStackRenderState();
    public int chiselProgress;
    public @Nullable Direction hitDirection;

    public ChiselableBlockRenderState() {
    }
}
