package com.marbledhubb.antiquus.world.level.block.entity.custom;

import com.marbledhubb.antiquus.world.level.block.entity.ModBlockEntityTypes;
import com.marbledhubb.antiquus.world.level.block.state.properties.ModBlockStateProperties;
import com.marbledhubb.antiquus.world.level.block.custom.ChiselableBlock;
import com.marbledhubb.antiquus.network.payload.ChiselBlockCompletePayload;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;

public class ChiselableBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String LOOT_TABLE_TAG = "LootTable";
    private static final String LOOT_TABLE_SEED_TAG = "LootTableSeed";
    private static final String HIT_DIRECTION_TAG = "hit_direction";
    private static final String ITEM_TAG = "item";
    private static final int CHISEL_COOLDOWN_TICKS = 10;
    private static final int CHISEL_HAMMER_ONLY_COOLDOWN_TICKS = 20;
    private static final int CHISEL_RESET_TICKS = 40;
    private static final int REQUIRED_CHISELS_TO_BREAK = 10;
    private int chiselCount;
    private long chiselCountResetsAtTick;
    private long coolDownEndsAtTick;
    private ItemStack item;
    private @Nullable Direction hitDirection;
    private ResourceKey<LootTable> lootTable;
    private long lootTableSeed;

    public ChiselableBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(ModBlockEntityTypes.CHISELABLE_BLOCK.get(), worldPosition, blockState);
        this.item = ItemStack.EMPTY;
    }

    public boolean chisel(long gameTime, ServerLevel level, LivingEntity user, Direction direction, ItemStack hammer, ItemStack chisel) {
        if (this.hitDirection == null) {
            this.hitDirection = direction;
        }

        this.chiselCountResetsAtTick = gameTime + CHISEL_RESET_TICKS;
        if (gameTime < this.coolDownEndsAtTick) {
            return false;
        } else {
            if (chisel.isEmpty() && level.getRandom().nextInt(15) == 0) {
                this.chiselingCompleted(level, user, hammer, false);
                return true;
            }

            this.coolDownEndsAtTick = gameTime + (chisel.isEmpty() ? CHISEL_HAMMER_ONLY_COOLDOWN_TICKS : CHISEL_COOLDOWN_TICKS);
            this.unpackLootTable(level, user, hammer);
            int previousCompletionState = this.getCompletionState();
            if (++this.chiselCount >= REQUIRED_CHISELS_TO_BREAK) {
                this.chiselingCompleted(level, user, hammer, true);
                return true;
            } else {
                level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 2);
                int completionState = this.getCompletionState();
                if (previousCompletionState != completionState) {
                    BlockState previousState = this.getBlockState();
                    BlockState state = previousState.setValue(ModBlockStateProperties.CHISELED, completionState);
                    level.setBlock(this.getBlockPos(), state, 3);
                }

                return false;
            }
        }
    }

    private void unpackLootTable(ServerLevel level, LivingEntity user, ItemInstance hammer) {
        if (this.lootTable != null) {
            LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(this.lootTable);
            if (user instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, this.lootTable);
            }

            LootParams params = (new LootParams.Builder(level)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.worldPosition)).withLuck(user.getLuck()).withParameter(LootContextParams.THIS_ENTITY, user).withParameter(LootContextParams.TOOL, hammer).create(LootContextParamSets.ARCHAEOLOGY);
            ObjectArrayList<ItemStack> loot = lootTable.getRandomItems(params, this.lootTableSeed);

            this.item = switch (loot.size()) {
                case 0 -> ItemStack.EMPTY;
                case 1 -> loot.getFirst();
                default -> {
                    LOGGER.warn("Expected max 1 loot from loot table {}, but got {}", this.lootTable.identifier(), loot.size());
                    yield loot.getFirst();
                }
            };
            this.lootTable = null;
            this.setChanged();
        }

    }

    private void chiselingCompleted(ServerLevel level, LivingEntity user, ItemStack hammer, boolean dropContent) {
        if (dropContent) this.dropContent(level, user, hammer);
        PacketDistributor.sendToAllPlayers(new ChiselBlockCompletePayload(this.getBlockPos()));
        Block turnsInto;
        if (this.getBlockState().getBlock() instanceof ChiselableBlock chiselableBlock) {
            turnsInto = chiselableBlock.getTurnsInto();
        } else {
            turnsInto = Blocks.AIR;
        }

        level.setBlock(this.worldPosition, turnsInto.defaultBlockState(), Block.UPDATE_ALL);
    }

    private void dropContent(ServerLevel level, LivingEntity user, ItemStack hammer) {
        this.unpackLootTable(level, user, hammer);
        if (!this.item.isEmpty()) {
            double size = EntityTypes.ITEM.getWidth();
            double centerRange = (double)1.0F - size;
            double halfSize = size / (double)2.0F;
            Direction dropDirection = Objects.requireNonNullElse(this.hitDirection, Direction.UP);
            BlockPos dropPos = this.worldPosition.relative(dropDirection, 1);
            double xo = (double)dropPos.getX() + (double)0.5F * centerRange + halfSize;
            double yo = (double)dropPos.getY() + (double)0.5F + (double)(EntityTypes.ITEM.getHeight() / 2.0F);
            double zo = (double)dropPos.getZ() + (double)0.5F * centerRange + halfSize;
            ItemEntity entity = new ItemEntity(level, xo, yo, zo, this.item.split(level.getRandom().nextInt(21) + 10));
            entity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(entity);
            this.item = ItemStack.EMPTY;
        }

    }

    public void checkReset(ServerLevel level) {
        if (this.chiselCount != 0 && level.getGameTime() >= this.chiselCountResetsAtTick) {
            int previousCompletionState = this.getCompletionState();
            this.chiselCount = Math.max(0, this.chiselCount - 2);
            int completionState = this.getCompletionState();
            if (previousCompletionState != completionState) {
                level.setBlock(this.getBlockPos(), this.getBlockState().setValue(ModBlockStateProperties.CHISELED, completionState), 3);
            }

            this.chiselCountResetsAtTick = level.getGameTime() + 4L;
        }

        if (this.chiselCount == 0) {
            this.hitDirection = null;
            this.chiselCountResetsAtTick = 0;
            this.coolDownEndsAtTick = 0;
        } else {
            level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 2);
        }

    }

    private boolean tryLoadLootTable(ValueInput input) {
        this.lootTable = input.read(LOOT_TABLE_TAG, LootTable.KEY_CODEC).orElse(null);
        this.lootTableSeed = input.getLongOr(LOOT_TABLE_SEED_TAG, 0L);
        return this.lootTable != null;
    }

    private boolean trySaveLootTable(ValueOutput base) {
        if (this.lootTable == null) {
            return false;
        } else {
            base.store(LOOT_TABLE_TAG, LootTable.KEY_CODEC, this.lootTable);
            if (this.lootTableSeed != 0L) {
                base.putLong(LOOT_TABLE_SEED_TAG, this.lootTableSeed);
            }

            return true;
        }
    }

    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.storeNullable(HIT_DIRECTION_TAG, Direction.LEGACY_ID_CODEC, this.hitDirection);
        if (!this.item.isEmpty()) {
            RegistryOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
            tag.store(ITEM_TAG, ItemStack.CODEC, ops, this.item);
        }

        return tag;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        if (!this.tryLoadLootTable(input)) {
            this.item = input.read(ITEM_TAG, ItemStack.CODEC).orElse(ItemStack.EMPTY);
        } else {
            this.item = ItemStack.EMPTY;
        }

        this.hitDirection = input.read(HIT_DIRECTION_TAG, Direction.LEGACY_ID_CODEC).orElse(null);
    }

    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        if (!this.trySaveLootTable(output) && !this.item.isEmpty()) {
            output.store(ITEM_TAG, ItemStack.CODEC, this.item);
        }

    }

    public void setLootTable(ResourceKey<LootTable> lootTable, long seed) {
        this.lootTable = lootTable;
        this.lootTableSeed = seed;
    }

    private int getCompletionState() {
        if (this.chiselCount == 0) {
            return 0;
        } else if (this.chiselCount < 3) {
            return 1;
        } else {
            return this.chiselCount < 6 ? 2 : 3;
        }
    }

    public @Nullable Direction getHitDirection() {
        return this.hitDirection;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
