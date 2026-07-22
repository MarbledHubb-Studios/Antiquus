package com.marbledhubb.antiquus.init.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.marbledhubb.antiquus.init.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class OldMossyCarpetBlock extends Block {
    public static final MapCodec<OldMossyCarpetBlock> CODEC = simpleCodec(OldMossyCarpetBlock::new);
    public static final BooleanProperty BASE = BlockStateProperties.BOTTOM;
    public static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
    public static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
    public static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
    public static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
    public static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
    private final Function<BlockState, VoxelShape> shapes;

    public MapCodec<OldMossyCarpetBlock> codec() {
        return CODEC;
    }

    public OldMossyCarpetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(BASE, true)).setValue(NORTH, WallSide.NONE)).setValue(EAST, WallSide.NONE)).setValue(SOUTH, WallSide.NONE)).setValue(WEST, WallSide.NONE));
        this.shapes = this.makeShapes();
    }

    public Function<BlockState, VoxelShape> makeShapes() {
        Map<Direction, VoxelShape> low = Shapes.rotateHorizontal(Block.boxZ(16.0, 0.0, 10.0, 0.0, 1.0));
        return this.getShapeForEachState((state) -> {
            VoxelShape shape = (Boolean)state.getValue(BASE) ? (VoxelShape)low.get(Direction.DOWN) : Shapes.empty();
            Iterator i$ = PROPERTY_BY_DIRECTION.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<Direction, EnumProperty<WallSide>> entry = (Map.Entry)i$.next();
                switch ((WallSide)state.getValue((Property)entry.getValue())) {
                    case NONE:
                    default:
                        break;
                    case LOW:
                        shape = Shapes.or(shape, (VoxelShape)low.get(entry.getKey()));
                }
            }

            return shape.isEmpty() ? Shapes.block() : shape;
        });
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (VoxelShape)this.shapes.apply(state);
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (Boolean)state.getValue(BASE) ? (VoxelShape)this.shapes.apply(this.defaultBlockState()) : Shapes.empty();
    }

    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.getBlockState(pos.below());
        return (Boolean)state.getValue(BASE) ? !belowState.isAir() : belowState.is(this) && (Boolean)belowState.getValue(BASE);
    }

    private static boolean hasFaces(BlockState blockState) {
        if ((Boolean)blockState.getValue(BASE)) {
            return true;
        } else {
            Iterator var1 = PROPERTY_BY_DIRECTION.values().iterator();

            EnumProperty property;
            do {
                if (!var1.hasNext()) {
                    return false;
                }

                property = (EnumProperty)var1.next();
            } while(blockState.getValue(property) == WallSide.NONE);

            return true;
        }
    }

    private static boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
        return direction == Direction.UP ? false : MultifaceBlock.canAttachTo(level, pos, direction);
    }

    private static BlockState getUpdatedState(BlockState state, BlockGetter level, BlockPos pos, boolean createSides) {
        BlockState aboveState = null;
        BlockState belowState = null;
        createSides |= (Boolean)state.getValue(BASE);

        EnumProperty property;
        WallSide side;
        for(Iterator var6 = Direction.Plane.HORIZONTAL.iterator(); var6.hasNext(); state = (BlockState)state.setValue(property, side)) {
            Direction direction = (Direction)var6.next();
            property = getPropertyForFace(direction);
            side = canSupportAtFace(level, pos, direction) ? (createSides ? WallSide.LOW : (WallSide)state.getValue(property)) : WallSide.NONE;
            if (side == WallSide.LOW) {
                if (aboveState == null) {
                    aboveState = level.getBlockState(pos.above());
                }

                if (!(Boolean)state.getValue(BASE)) {
                    if (belowState == null) {
                        belowState = level.getBlockState(pos.below());
                    }

                    if (belowState.is(ModBlocks.OLD_MOSS_CARPET) && belowState.getValue(property) == WallSide.NONE) {
                        side = WallSide.NONE;
                    }
                }
            }
        }

        return state;
    }

    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return getUpdatedState(this.defaultBlockState(), context.getLevel(), context.getClickedPos(), true);
    }

    public static void placeAt(LevelAccessor level, BlockPos pos, RandomSource random, @UpdateFlags int updateType) {
        BlockState simpleCarpetLayer = ModBlocks.OLD_MOSS_CARPET.get().defaultBlockState();
        BlockState adjustedCarpetLayer = getUpdatedState(simpleCarpetLayer, level, pos, true);
        level.setBlock(pos, adjustedCarpetLayer, updateType);
        Objects.requireNonNull(random);
        BlockState state = createTopperWithSideChance(level, pos, random::nextBoolean);
        if (!state.isAir()) {
            level.setBlock(pos.above(), state, updateType);
            BlockState updateBottomCarpet = getUpdatedState(adjustedCarpetLayer, level, pos, true);
            level.setBlock(pos, updateBottomCarpet, updateType);
        }

    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack) {
        if (!level.isClientSide()) {
            RandomSource random = level.getRandom();
            Objects.requireNonNull(random);
            BlockState topper = createTopperWithSideChance(level, pos, random::nextBoolean);
            if (!topper.isAir()) {
                level.setBlock(pos.above(), topper, 3);
            }
        }

    }

    private static BlockState createTopperWithSideChance(BlockGetter level, BlockPos pos, BooleanSupplier sideSurvivalTest) {
        BlockPos above = pos.above();
        BlockState abovePreviousState = level.getBlockState(above);
        boolean isMossyCarpetAbove = abovePreviousState.is(ModBlocks.OLD_MOSS_CARPET);
        if ((!isMossyCarpetAbove || !(Boolean)abovePreviousState.getValue(BASE)) && (isMossyCarpetAbove || abovePreviousState.canBeReplaced())) {
            BlockState noCarpetBaseState = (BlockState)ModBlocks.OLD_MOSS_CARPET.get().defaultBlockState().setValue(BASE, false);
            BlockState aboveState = getUpdatedState(noCarpetBaseState, level, pos.above(), true);
            Iterator var8 = Direction.Plane.HORIZONTAL.iterator();

            while(var8.hasNext()) {
                Direction direction = (Direction)var8.next();
                EnumProperty<WallSide> property = getPropertyForFace(direction);
                if (aboveState.getValue(property) != WallSide.NONE && !sideSurvivalTest.getAsBoolean()) {
                    aboveState = (BlockState)aboveState.setValue(property, WallSide.NONE);
                }
            }

            return hasFaces(aboveState) && aboveState != abovePreviousState ? aboveState : Blocks.AIR.defaultBlockState();
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState blockState = getUpdatedState(state, level, pos, false);
            return !hasFaces(blockState) ? Blocks.AIR.defaultBlockState() : blockState;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{BASE, NORTH, EAST, SOUTH, WEST});
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        BlockState var10000;
        switch (rotation) {
            case CLOCKWISE_180 -> var10000 = (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(SOUTH))).setValue(EAST, (WallSide)state.getValue(WEST))).setValue(SOUTH, (WallSide)state.getValue(NORTH))).setValue(WEST, (WallSide)state.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> var10000 = (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(EAST))).setValue(EAST, (WallSide)state.getValue(SOUTH))).setValue(SOUTH, (WallSide)state.getValue(WEST))).setValue(WEST, (WallSide)state.getValue(NORTH));
            case CLOCKWISE_90 -> var10000 = (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(WEST))).setValue(EAST, (WallSide)state.getValue(NORTH))).setValue(SOUTH, (WallSide)state.getValue(EAST))).setValue(WEST, (WallSide)state.getValue(SOUTH));
            default -> var10000 = state;
        }

        return var10000;
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        BlockState var10000;
        switch (mirror) {
            case LEFT_RIGHT -> var10000 = (BlockState)((BlockState)state.setValue(NORTH, (WallSide)state.getValue(SOUTH))).setValue(SOUTH, (WallSide)state.getValue(NORTH));
            case FRONT_BACK -> var10000 = (BlockState)((BlockState)state.setValue(EAST, (WallSide)state.getValue(WEST))).setValue(WEST, (WallSide)state.getValue(EAST));
            default -> var10000 = super.mirror(state, mirror);
        }

        return var10000;
    }

    public static @Nullable EnumProperty<WallSide> getPropertyForFace(Direction direction) {
        return (EnumProperty)PROPERTY_BY_DIRECTION.get(direction);
    }
}
