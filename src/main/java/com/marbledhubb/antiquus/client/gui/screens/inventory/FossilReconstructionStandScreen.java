package com.marbledhubb.antiquus.client.gui.screens.inventory;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.world.inventory.custom.FossilReconstructionStandMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class FossilReconstructionStandScreen extends AbstractContainerScreen<FossilReconstructionStandMenu> {
    private static final Identifier MEDIUM_LENGTH_SPRITE = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "container/fossil_reconstruction_stand/medium_length");
    private static final Identifier RECONSTRUCTION_PROGRESS_SPRITE = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "container/fossil_reconstruction_stand/reconstruction_progress");
    private static final Identifier FOSSIL_RECONSTRUCTION_STAND_LOCATION = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "textures/gui/container/fossil_reconstruction_stand.png");

    public FossilReconstructionStandScreen(FossilReconstructionStandMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, FOSSIL_RECONSTRUCTION_STAND_LOCATION, xo, yo, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int reconstructionMedium = this.menu.getReconstructionMedium();
        int reconstructionMediumLength = Mth.clamp((18 * reconstructionMedium + 20 - 1) / 20, 0, 18);
        if (reconstructionMediumLength > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MEDIUM_LENGTH_SPRITE, 18, 4, 0, 0, xo + 56, yo + 65, reconstructionMediumLength, 4);
        }

        int tickCount = this.menu.getReconstructionTicks();
        if (tickCount > 0) {
            int length = (int)(28f * (1f - (float)tickCount / 400f));
            if (length > 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RECONSTRUCTION_PROGRESS_SPRITE, 9, 28, 0, 0, xo + 120, yo + 23, 9, length);
            }
        }
    }
}
