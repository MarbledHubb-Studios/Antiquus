package com.marbledhubb.antiquus.world.level.block.custom;

import com.marbledhubb.antiquus.stats.ModStats;
import com.marbledhubb.antiquus.world.level.block.entity.ModBlockEntityTypes;
import com.marbledhubb.antiquus.world.level.block.entity.custom.FossilReconstructionStandBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class FossilReconstructionStandBlock extends BaseEntityBlock {
    public static final MapCodec<FossilReconstructionStandBlock> CODEC = simpleCodec(FossilReconstructionStandBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Shapes.or(
            Block.box(4, 0, 3, 12, 2, 11),
            Block.box(6, 0, 11, 10, 10, 14),
            Block.box(7, 7, 3, 9, 9, 11),
            Block.box(7, 4, 6, 9, 7, 8)));

    @Override
    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public FossilReconstructionStandBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new FossilReconstructionStandBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntityTypes.FOSSIL_RECONSTRUCTION_STAND.get(), FossilReconstructionStandBlockEntity::serverTick);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        Direction clickedFace = context.getClickedFace();
        if (!context.replacingClickedOnBlock() && clickedFace.getAxis().isHorizontal()) {
            state = state.setValue(FACING, clickedFace);
        } else {
            state = state.setValue(FACING, context.getHorizontalDirection().getOpposite());
        }

        return state;
    }

    @Override
    protected @NonNull VoxelShape getShape(BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof FossilReconstructionStandBlockEntity blockEntity) {
                player.openMenu(blockEntity);
                player.awardStat(ModStats.INTERACT_WITH_FOSSIL_RECONSTRUCTION_STAND.get());
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NonNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected boolean isPathfindable(@NonNull BlockState state, @NonNull PathComputationType type) {
        return false;
    }
}
