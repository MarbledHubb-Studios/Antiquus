package com.marbledhubb.antiquus.world.inventory.custom;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.tags.ModItemTags;
import com.marbledhubb.antiquus.world.inventory.ModMenuTypes;
import com.marbledhubb.antiquus.world.item.crafting.ModRecipePropertySets;
import com.marbledhubb.antiquus.world.level.block.entity.custom.FossilAnalysisStandBlockEntity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.jspecify.annotations.NonNull;

public class FossilAnalysisStandMenu extends AbstractContainerMenu {
    private static final Identifier EMPTY_SLOT_RECONSTRUCTION_MEDIUM = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "container/slot/reconstruction_medium");
    private static final Identifier EMPTY_SLOT_FOSSIL = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "container/slot/fossil");
    private static final int FOSSIL_SLOT = 0;
    private static final int ANALOGUE_SLOT = 1;
    private static final int RECONSTRUCTION_MEDIUM_SLOT = 2;
    private static final int RESULT_SLOT = 3;
    private static final int SLOT_COUNT = 4;
    private static final int DATA_COUNT = 2; // TODO
    private static final int INV_SLOT_START = 4;
    private static final int INV_SLOT_END = 31;
    private static final int USE_ROW_SLOT_START = 31;
    private static final int USE_ROW_SLOT_END = 40;
    private final Container fossilAnalysisStand;
    private final ContainerData fossilAnalysisStandData;
    private final Slot fossilSlot; // TODO

    public FossilAnalysisStandMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(5), new SimpleContainerData(2));
    }

    public FossilAnalysisStandMenu(int containerId, Inventory inventory, Container fossilAnalysisStand, ContainerData fossilAnalysisStandData) {
        super(ModMenuTypes.FOSSIL_ANALYSIS_STAND.get(), containerId);
        checkContainerSize(fossilAnalysisStand, SLOT_COUNT);
        checkContainerDataCount(fossilAnalysisStandData, DATA_COUNT);
        this.fossilAnalysisStand = fossilAnalysisStand;
        this.fossilAnalysisStandData = fossilAnalysisStandData;
        this.fossilSlot = this.addSlot(new FossilSlot(inventory.player.level().recipeAccess(), fossilAnalysisStand, FOSSIL_SLOT, 79, 17));
        this.addSlot(new Slot(fossilAnalysisStand, ANALOGUE_SLOT, 102, 24));
        this.addSlot(new ReconstructionMediumSlot(fossilAnalysisStand, RECONSTRUCTION_MEDIUM_SLOT, 56, 24));
        this.addSlot(new Slot(fossilAnalysisStand, RESULT_SLOT, 79, 58));
        this.addDataSlots(fossilAnalysisStandData);
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.fossilAnalysisStand.stillValid(player);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex >= INV_SLOT_START) {
                // TODO
                //if (ReconstructionMediumSlot.mayPlaceItem(clicked)) {
                //    if (this.moveItemStackTo(stack, 4, 5, false) || this.fossilSlot.mayPlace(stack) && !this.moveItemStackTo(stack, 3, 4, false)) {
                //        return ItemStack.EMPTY;
                //    }
                //} else if (this.fossilSlot.mayPlace(stack)) {
                //    if (!this.moveItemStackTo(stack, 3, 4, false)) {
                //        return ItemStack.EMPTY;
                //    }
                //} else if (ReconstructionMediumSlot.mayPlaceItem(player.level().potionBrewing(), clicked)) {
                //    if (!this.moveItemStackTo(stack, 0, 3, false)) {
                //        return ItemStack.EMPTY;
                //    }
                //} else if (slotIndex >= 5 && slotIndex < 32) {
                //    if (!this.moveItemStackTo(stack, 32, 41, false)) {
                //        return ItemStack.EMPTY;
                //    }
                //} else if (slotIndex >= 32 && slotIndex < 41) {
                //    if (!this.moveItemStackTo(stack, 5, 32, false)) {
                //        return ItemStack.EMPTY;
                //    }
                //} else if (!this.moveItemStackTo(stack, 5, 41, false)) {
                //    return ItemStack.EMPTY;
                //}
            } else {
                if (!this.moveItemStackTo(stack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, clicked);
        }

        return clicked;
    }

    public int getReconstructionMedium() {
        Antiquus.LOGGER.info("reconstruction medium is: " + this.fossilAnalysisStandData.get(FossilAnalysisStandBlockEntity.DATA_RECONSTRUCTION_MEDIUM_USES));
        return this.fossilAnalysisStandData.get(FossilAnalysisStandBlockEntity.DATA_RECONSTRUCTION_MEDIUM_USES);
    }

    public int getReconstructionTicks() {
        return this.fossilAnalysisStandData.get(FossilAnalysisStandBlockEntity.DATA_RECONSTRUCTION_TIME);
    }

    private static class ReconstructionMediumSlot extends Slot {
        public ReconstructionMediumSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(@NonNull ItemStack itemStack) {
            return mayPlaceItem(itemStack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.is(ModItemTags.FOSSIL_RECONSTRUCTION_MEDIUM);
        }

        @Override
        public Identifier getNoItemIcon() {
            return FossilAnalysisStandMenu.EMPTY_SLOT_RECONSTRUCTION_MEDIUM;
        }
    }

    private static class FossilSlot extends Slot {
        private final RecipePropertySet propertySet;

        public FossilSlot(RecipeAccess recipeAccess, Container container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.propertySet = recipeAccess.propertySet(ModRecipePropertySets.FOSSIL_RECONSTRUCTION_FOSSIL);
        }

        @Override
        public boolean mayPlace(@NonNull ItemStack itemStack) {
            return this.propertySet.test(itemStack);
        }

        @Override
        public Identifier getNoItemIcon() {
            return FossilAnalysisStandMenu.EMPTY_SLOT_FOSSIL;
        }
    }
}
