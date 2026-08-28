package com.marbledhubb.antiquus.world.level.block.entity.custom;

import com.marbledhubb.antiquus.world.inventory.custom.FossilAnalysisStandMenu;
import com.marbledhubb.antiquus.world.item.crafting.ModRecipePropertySets;
import com.marbledhubb.antiquus.world.item.crafting.ModRecipeTypes;
import com.marbledhubb.antiquus.world.item.crafting.custom.FossilReconstructionRecipe;
import com.marbledhubb.antiquus.world.item.crafting.custom.FossilReconstructionRecipeInput;
import com.marbledhubb.antiquus.world.level.block.entity.ModBlockEntityTypes;
import com.marbledhubb.antiquus.tags.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FossilAnalysisStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int FOSSIL_SLOT = 0;
    public static final int ANALOGUE_SLOT = 1;
    public static final int RECONSTRUCTION_MEDIUM_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    private static final int[] SLOTS_FOR_UP = new int[]{FOSSIL_SLOT};
    private static final int[] SLOTS_FOR_DOWN = new int[]{FOSSIL_SLOT, RESULT_SLOT};
    private static final int[] SLOTS_FOR_SIDES = new int[]{ANALOGUE_SLOT, RECONSTRUCTION_MEDIUM_SLOT, RESULT_SLOT};
    private static final int RECONSTRUCTION_MEDIUM_USES = 20;
    private static final int RECONSTRUCTION_DURATION = 400;
    public static final int DATA_RECONSTRUCTION_TIME = 0;
    public static final int DATA_RECONSTRUCTION_MEDIUM_USES = 1;
    public static final int NUM_DATA_VALUES = 2;
    private static final Component DEFAULT_NAME = Component.translatable("container.fossil_analysis");
    private NonNullList<ItemStack> items;
    private int reconstructionTime;
    private Item fossil;
    private Item analogue;
    private int reconstructionMedium;
    protected final ContainerData dataAccess;

    public FossilAnalysisStandBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.FOSSIL_ANALYSIS_STAND.get(), worldPosition, blockState);
        this.items = NonNullList.withSize(4, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            @Override
            public int get(int dataId) {
                int i;
                switch (dataId) {
                    case DATA_RECONSTRUCTION_TIME -> i = FossilAnalysisStandBlockEntity.this.reconstructionTime;
                    case DATA_RECONSTRUCTION_MEDIUM_USES -> i = FossilAnalysisStandBlockEntity.this.reconstructionMedium;
                    default -> i = 0;
                }

                return i;
            }

            @Override
            public void set(int dataId, int value) {
                switch (dataId) {
                    case DATA_RECONSTRUCTION_TIME -> FossilAnalysisStandBlockEntity.this.reconstructionTime = value;
                    case DATA_RECONSTRUCTION_MEDIUM_USES -> FossilAnalysisStandBlockEntity.this.reconstructionMedium = value;
                }

            }

            @Override
            public int getCount() {
                return NUM_DATA_VALUES;
            }
        };
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.items = items;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState selfState, FossilAnalysisStandBlockEntity entity) {
        if (entity.reconstructionMedium <= 0 && entity.items.get(RECONSTRUCTION_MEDIUM_SLOT).is(ModItemTags.FOSSIL_RECONSTRUCTION_MEDIUM)) {
            entity.reconstructionMedium = RECONSTRUCTION_MEDIUM_USES;
            shrinkSlot(level, pos, entity.items, RECONSTRUCTION_MEDIUM_SLOT);
            setChanged(level, pos, selfState);
        }

        Optional<RecipeHolder<FossilReconstructionRecipe>> recipe = getRecipe(level, entity.items);
        ItemStack resultStack = entity.items.get(RESULT_SLOT);
        boolean reconstructable = recipe.isPresent() && (resultStack.isEmpty() || (resultStack.getItem() == recipe.get().value().getResult().item().value() && resultStack.getCount() < resultStack.getMaxStackSize()));
        if (entity.reconstructionTime > 0) {
            --entity.reconstructionTime;
            if (entity.reconstructionTime <= 0 && reconstructable) {
                doReconstruction(level, pos, entity.items, recipe.get(), resultStack);
            } else if (!reconstructable || !entity.items.get(FOSSIL_SLOT).is(entity.fossil) || !entity.items.get(ANALOGUE_SLOT).is(entity.analogue)) {
                entity.reconstructionTime = 0;
            }

            setChanged(level, pos, selfState);
        } else if (reconstructable && entity.reconstructionMedium > 0) {
            --entity.reconstructionMedium;
            entity.reconstructionTime = RECONSTRUCTION_DURATION;
            entity.fossil = entity.items.get(FOSSIL_SLOT).getItem();
            entity.analogue = entity.items.get(ANALOGUE_SLOT).getItem();
            setChanged(level, pos, selfState);
        }

        // TODO possible future changes on the blockstate's properties -aimi
    }

    private static Optional<RecipeHolder<FossilReconstructionRecipe>> getRecipe(Level level, NonNullList<ItemStack> items) {
        return ((RecipeManager) level.recipeAccess()).getRecipeFor(ModRecipeTypes.FOSSIL_RECONSTRUCTION.get(), new FossilReconstructionRecipeInput(items.get(FOSSIL_SLOT), items.get(ANALOGUE_SLOT)), level);
    }

    private static void doReconstruction(Level level, BlockPos pos, NonNullList<ItemStack> items, RecipeHolder<FossilReconstructionRecipe> recipe, ItemStack resultStack) {
        shrinkSlot(level, pos, items, FOSSIL_SLOT);
        shrinkSlot(level, pos, items, ANALOGUE_SLOT);
        FossilReconstructionRecipeInput input = new FossilReconstructionRecipeInput(items.get(FOSSIL_SLOT), items.get(ANALOGUE_SLOT));
        if (resultStack.isEmpty()) {
            items.set(RESULT_SLOT, recipe.value().assemble(input));
        } else {
            resultStack.grow(1);
        }

        //level.levelEvent(LevelEvent.SOUND_BREWING_STAND_BREW, pos, 0); TODO custom sound for when fossil reconstruction is finished -aimi
    }

    private static void shrinkSlot(Level level, BlockPos pos, NonNullList<ItemStack> items, int slotIndex) {
        ItemStack stack = items.get(slotIndex);
        ItemStackTemplate remainder = stack.getCraftingRemainder();
        stack.shrink(1);
        if (remainder != null) {
            if (stack.isEmpty()) {
                stack = remainder.create();
            } else {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainder.create());
            }
        }
        items.set(slotIndex, stack);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);

        ItemStack fossil = this.items.get(FOSSIL_SLOT);
        if (!fossil.isEmpty()) {
            List<ItemStackWithSlot> items = new ArrayList<>(1);
            items.add(new ItemStackWithSlot(
                    FOSSIL_SLOT,
                    fossil
            ));

            RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
            tag.store("Items", ItemStackWithSlot.CODEC.listOf(), ops, items);
        }

        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.reconstructionTime = input.getShortOr("ReconstructionTime", (short)0);
        if (this.reconstructionTime > 0) {
            this.fossil = this.items.get(FOSSIL_SLOT).getItem();
            this.analogue = this.items.get(ANALOGUE_SLOT).getItem();
        }

        this.reconstructionMedium = input.getByteOr("ReconstructionMedium", (byte)0);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putShort("ReconstructionTime", (short)this.reconstructionTime);
        ContainerHelper.saveAllItems(output, this.items);
        output.putByte("ReconstructionMedium", (byte)this.reconstructionMedium);
    }

    @Override
    public boolean canPlaceItem(int slot, @NonNull ItemStack itemStack) {
        return switch (slot) {
            case FOSSIL_SLOT -> this.level.recipeAccess().propertySet(ModRecipePropertySets.FOSSIL_RECONSTRUCTION_FOSSIL).test(itemStack);
            case ANALOGUE_SLOT -> true;
            case RECONSTRUCTION_MEDIUM_SLOT -> itemStack.is(ModItemTags.FOSSIL_RECONSTRUCTION_MEDIUM);
            default -> false;
        };
    }

    @Override
    public int @NonNull [] getSlotsForFace(@NonNull Direction direction) {
        return switch (direction) {
            case UP -> SLOTS_FOR_UP;
            case DOWN -> SLOTS_FOR_DOWN;
            default -> SLOTS_FOR_SIDES;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NonNull ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(slot, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NonNull ItemStack itemStack, @NonNull Direction direction) {
        return slot == RECONSTRUCTION_MEDIUM_SLOT || slot == RESULT_SLOT;
    }

    @Override
    protected @NonNull AbstractContainerMenu createMenu(int containerId, @NonNull Inventory inventory) {
        return new FossilAnalysisStandMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    public void setItem(int slot, @NonNull ItemStack itemStack, boolean insideTransaction) {
        super.setItem(slot, itemStack, insideTransaction);

        if (slot == FOSSIL_SLOT && level != null && !level.isClientSide()) {
            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_CLIENTS
            );
        }
    }
}
