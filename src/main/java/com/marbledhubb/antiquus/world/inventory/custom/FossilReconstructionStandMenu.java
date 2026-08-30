package com.marbledhubb.antiquus.world.inventory.custom;

import com.marbledhubb.antiquus.Antiquus;
import com.marbledhubb.antiquus.advancements.triggers.ModCriteriaTriggers;
import com.marbledhubb.antiquus.tags.ModItemTags;
import com.marbledhubb.antiquus.world.inventory.ModMenuTypes;
import com.marbledhubb.antiquus.world.item.crafting.ModRecipePropertySets;
import com.marbledhubb.antiquus.world.level.block.entity.custom.FossilReconstructionStandBlockEntity;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.item.crafting.RecipePropertySet;
import org.jspecify.annotations.NonNull;

public class FossilReconstructionStandMenu extends AbstractContainerMenu {
    private static final Identifier EMPTY_SLOT_RECONSTRUCTION_MEDIUM = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "container/slot/reconstruction_medium");
    private static final Identifier EMPTY_SLOT_FOSSIL = Identifier.fromNamespaceAndPath(Antiquus.MOD_ID, "container/slot/fossil");
    private static final int FOSSIL_SLOT = 0;
    private static final int ANALOGUE_SLOT = 1;
    private static final int RECONSTRUCTION_MEDIUM_SLOT = 2;
    private static final int RESULT_SLOT = 3;
    private static final int SLOT_COUNT = 4;
    private static final int INV_SLOT_START = 4;
    private static final int INV_SLOT_END = 31;
    private static final int USE_ROW_SLOT_START = 31;
    private static final int USE_ROW_SLOT_END = 40;
    private final Container fossilReconstructionStand;
    private final ContainerData fossilReconstructionStandData;

    public FossilReconstructionStandMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(FossilReconstructionStandBlockEntity.NUM_DATA_VALUES));
    }

    public FossilReconstructionStandMenu(int containerId, Inventory inventory, Container fossilReconstructionStand, ContainerData fossilReconstructionStandData) {
        super(ModMenuTypes.FOSSIL_RECONSTRUCTION_STAND.get(), containerId);
        checkContainerSize(fossilReconstructionStand, SLOT_COUNT);
        checkContainerDataCount(fossilReconstructionStandData, FossilReconstructionStandBlockEntity.NUM_DATA_VALUES);
        this.fossilReconstructionStand = fossilReconstructionStand;
        this.fossilReconstructionStandData = fossilReconstructionStandData;
        this.addSlot(new FossilSlot(inventory.player.level().recipeAccess(), fossilReconstructionStand, FOSSIL_SLOT, 79, 17));
        this.addSlot(new Slot(fossilReconstructionStand, ANALOGUE_SLOT, 102, 24));
        this.addSlot(new ReconstructionMediumSlot(fossilReconstructionStand, RECONSTRUCTION_MEDIUM_SLOT, 56, 24));
        this.addSlot(new ResultSlot(fossilReconstructionStand, RESULT_SLOT, 79, 58));
        this.addDataSlots(fossilReconstructionStandData);
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return this.fossilReconstructionStand.stillValid(player);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex >= INV_SLOT_START) {
                if (!(this.moveItemStackTo(stack, FOSSIL_SLOT, FOSSIL_SLOT + 1, false) ||
                        this.moveItemStackTo(stack, RECONSTRUCTION_MEDIUM_SLOT, RECONSTRUCTION_MEDIUM_SLOT +1, false) ||
                        this.moveItemStackTo(stack, ANALOGUE_SLOT, ANALOGUE_SLOT +1, false) ||
                        (slotIndex >= USE_ROW_SLOT_START && slotIndex < USE_ROW_SLOT_END && this.moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, false)) ||
                        this.moveItemStackTo(stack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false))) {
                    return ItemStack.EMPTY;
                }
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
        return this.fossilReconstructionStandData.get(FossilReconstructionStandBlockEntity.DATA_RECONSTRUCTION_MEDIUM_USES);
    }

    public int getReconstructionTicks() {
        return this.fossilReconstructionStandData.get(FossilReconstructionStandBlockEntity.DATA_RECONSTRUCTION_TIME);
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
            return FossilReconstructionStandMenu.EMPTY_SLOT_RECONSTRUCTION_MEDIUM;
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
            return FossilReconstructionStandMenu.EMPTY_SLOT_FOSSIL;
        }
    }

    private static class ResultSlot extends Slot {
        public ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(@NonNull ItemStack itemStack) {
            return false;
        }

        @Override
        public void onTake(@NonNull Player player, @NonNull ItemStack carried) {
            if (player instanceof ServerPlayer serverPlayer)
                ModCriteriaTriggers.RECONSTRUCTED_FOSSIL_CONTENT.get().trigger(serverPlayer, carried);

            super.onTake(player, carried);
        }
    }
}
