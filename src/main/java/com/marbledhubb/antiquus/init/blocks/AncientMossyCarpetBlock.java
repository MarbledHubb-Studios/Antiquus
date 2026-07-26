package com.marbledhubb.antiquus.init.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.marbledhubb.antiquus.init.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class AncientMossyCarpetBlock extends Block {
    public static final MapCodec<AncientMossyCarpetBlock> CODEC = simpleCodec(AncientMossyCarpetBlock::new);
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
    private final Function<BlockState, VoxelShape> shapes;

    @Override
    public @NonNull MapCodec<AncientMossyCarpetBlock> codec() {
        return CODEC;
    }

    public AncientMossyCarpetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
        this.shapes = this.makeShapes();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    public Function<BlockState, VoxelShape> makeShapes() {
        Map<Direction, VoxelShape> directionShapes = Shapes.rotateHorizontal(Block.boxZ(16.0, 0.0, 10.0, 0.0, 1.0));
        return this.getShapeForEachState((state) -> {
            VoxelShape shape = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

            for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
                if (state.getValue(entry.getValue())) {
                    shape = Shapes.or(shape, directionShapes.get(entry.getKey()));
                }
            }

            return shape;
        });
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return this.shapes.apply(state);
    }

    @Override
    protected @NonNull VoxelShape getCollisionShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return this.shapes.apply(this.defaultBlockState());
    }

    @Override
    protected boolean propagatesSkylightDown(@NonNull BlockState state) {
        return true;
    }

    @Override
    protected boolean canSurvive(@NonNull BlockState state, LevelReader level, BlockPos pos) {
        return !level.getBlockState(pos.below()).isAir();
    }

    private static boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
        return direction != Direction.UP && MultifaceBlock.canAttachTo(level, pos, direction);
    }

    private static BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos) {
        BooleanProperty property;
        boolean side;
        for(Iterator<Direction> var6 = Direction.Plane.HORIZONTAL.iterator(); var6.hasNext(); state = state.setValue(property, side)) {
            Direction direction = var6.next();
            property = getPropertyForFace(direction);
            side = canSupportAtFace(level, pos, direction);
        }

        return state;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getUpdatedState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    // TODO unused. should we remove it? -aimi
    // TODO yes, it can be removed -mn
    public static void placeAt(LevelAccessor level, BlockPos pos, @UpdateFlags int updateType) {
        BlockState simpleCarpetLayer = ModBlocks.ANCIENT_MOSS_CARPET.get().defaultBlockState();
        BlockState adjustedCarpetLayer = getUpdatedState(simpleCarpetLayer, level, pos);
        level.setBlock(pos, adjustedCarpetLayer, updateType);
    }

    @Override
    protected @NonNull BlockState updateShape(BlockState state, @NonNull LevelReader level, @NonNull ScheduledTickAccess ticks, @NonNull BlockPos pos, @NonNull Direction directionToNeighbour, @NonNull BlockPos neighbourPos, @NonNull BlockState neighbourState, @NonNull RandomSource random) {
        return state.canSurvive(level, pos) ? getUpdatedState(state, level, pos) : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected @NonNull BlockState rotate(@NonNull BlockState state, Rotation rotation) {
        BlockState var10000;
        switch (rotation) {
            case CLOCKWISE_180 -> var10000 = state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> var10000 = state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            case CLOCKWISE_90 -> var10000 = state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            default -> var10000 = state;
        }

        return var10000;
    }

    @Override
    protected @NonNull BlockState mirror(@NonNull BlockState state, Mirror mirror) {
        BlockState var10000;
        switch (mirror) {
            case LEFT_RIGHT -> var10000 = state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> var10000 = state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> var10000 = super.mirror(state, mirror);
        }

        return var10000;
    }

    public static @Nullable BooleanProperty getPropertyForFace(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }
}
