package com.marbledhubb.antiquus.world.item.custom;

import com.marbledhubb.antiquus.world.item.ModItemAbilities;
import com.marbledhubb.antiquus.world.item.ModItemUseAnimations;
import com.marbledhubb.antiquus.world.level.block.custom.ChiselableBlock;
import com.marbledhubb.antiquus.world.level.block.entity.custom.ChiselableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.NonNull;

public class RockHammerItem extends Item {
    public static final int ANIMATION_DURATION = 10;
    public static final int ANIMATION_IMPACT_TICK = Math.round(ANIMATION_DURATION * 0.25f);

    public RockHammerItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && this.calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
            player.startUsingItem(context.getHand());
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public @NonNull ItemUseAnimation getUseAnimation(@NonNull ItemStack itemStack) {
        return ModItemUseAnimations.ROCK_HAMMER;
    }

    @Override
    public int getUseDuration(@NonNull ItemStack itemStack, @NonNull LivingEntity user) {
        return 200;
    }

    @Override
    public void onUseTick(@NonNull Level level, @NonNull LivingEntity entity, @NonNull ItemStack stack, int ticksRemaining) {
        if (ticksRemaining >= 0 && entity instanceof Player player) {
            HitResult hitResult = this.calculateHitResult(player);
            if (hitResult instanceof BlockHitResult blockHitResult) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    int timeElapsed = this.getUseDuration(stack, entity) - ticksRemaining + 1;
                    boolean isImpactTick = timeElapsed % ANIMATION_DURATION == ANIMATION_IMPACT_TICK;
                    if (isImpactTick) {
                        BlockPos pos = blockHitResult.getBlockPos();
                        BlockState state = level.getBlockState(pos);
                        HumanoidArm chiselingArm = entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                        if (state.shouldSpawnTerrainParticles() && state.getRenderShape() != RenderShape.INVISIBLE) {
                            this.spawnDustParticles(level, blockHitResult, state, entity.getViewVector(0.0F), chiselingArm);
                        }

                        SoundEvent chiselSound;
                        if (state.getBlock() instanceof ChiselableBlock block) {
                            chiselSound = block.getChiselSound();
                        } else {
                            chiselSound = state.getSoundType(level, pos, entity).getStepSound();
                        }

                        level.playSound(player, pos, chiselSound, SoundSource.BLOCKS, 0.5f, 1f);
                        if (level instanceof ServerLevel serverLevel) {
                            BlockEntity blockEntity = level.getBlockEntity(pos);
                            if (blockEntity instanceof ChiselableBlockEntity chiselableBlockEntity) {
                                ItemStack chisel = getChisel(entity);
                                if (chiselableBlockEntity.chisel(level.getGameTime(), serverLevel, player, blockHitResult.getDirection(), stack, chisel)) {
                                    if (chisel.isEmpty()) {
                                        stack.hurtAndBreak(2, player, stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);
                                    } else {
                                        stack.hurtAndBreak(1, player, stack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);
                                        chisel.hurtAndBreak(1, player, chisel.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);
                                    }
                                }
                            }

                            return;
                        }
                    }

                    return;
                }
            }

            entity.releaseUsingItem();
        } else {
            entity.releaseUsingItem();
        }

    }

    private HitResult calculateHitResult(Player player) {
        return ProjectileUtil.getHitResultOnViewVector(player, EntitySelector.CAN_BE_PICKED, player.blockInteractionRange());
    }

    private void spawnDustParticles(Level level, BlockHitResult hitResult, BlockState state, Vec3 viewVector, HumanoidArm chiselingArm) {
        int flip = chiselingArm == HumanoidArm.RIGHT ? 1 : -1;
        int particles = level.getRandom().nextInt(7, 12);
        BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, state);
        Direction hitDirection = hitResult.getDirection();
        DustParticlesDelta dustParticlesDelta = DustParticlesDelta.fromDirection(viewVector, hitDirection);
        Vec3 hitLocation = hitResult.getLocation();

        for(int i = 0; i < particles; ++i) {
            level.addParticle(particle, hitLocation.x - (double)(hitDirection == Direction.WEST ? 1.0E-6F : 0.0F), hitLocation.y, hitLocation.z - (double)(hitDirection == Direction.NORTH ? 1E-6f : 0f), dustParticlesDelta.xd() * (double)flip * (double)3f * level.getRandom().nextDouble(), 0, dustParticlesDelta.zd() * (double)flip * (double)3f * level.getRandom().nextDouble());
        }
    }

    @Override
    public boolean canPerformAction(@NonNull ItemInstance stack, @NonNull ItemAbility itemAbility) {
        return ModItemAbilities.DEFAULT_ROCK_HAMMER_ACTIONS.contains(itemAbility);
    }

    public ItemStack getChisel(LivingEntity entity) {
        ItemStack chisel = entity.getItemInHand(entity.getUsedItemHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if (!chisel.canPerformAction(ModItemAbilities.ROCK_CHISEL_CHISEL)) chisel = ItemStack.EMPTY;
        return chisel;
    }

    private record DustParticlesDelta(double xd, double yd, double zd) {
        public static DustParticlesDelta fromDirection(Vec3 viewVector, Direction hitDirection) {
            return switch (hitDirection) {
                case DOWN, UP -> new DustParticlesDelta(viewVector.z(), 0, -viewVector.x());
                case NORTH -> new DustParticlesDelta(1, 0, -0.1);
                case SOUTH -> new DustParticlesDelta(-1, 0, 0.1);
                case WEST -> new DustParticlesDelta(-0.1, 0, -1);
                case EAST -> new DustParticlesDelta(0.1, 0, 1);
            };
        }
    }
}
