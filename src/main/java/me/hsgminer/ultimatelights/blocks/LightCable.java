package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class LightCable extends AbstractLightBlock {

    //<editor-fold desc="properties">
    private static final BooleanProperty UP = BooleanProperty.create("up");
    private static final BooleanProperty DOWN = BooleanProperty.create("down");
    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty WEST = BooleanProperty.create("west");
    //</editor-fold>

    //<editor-fold desc="shapes">
    private static final VoxelShape SHAPE_CENTER = makeCuboidShape(5, 5, 5, 11, 11, 11);
    private static final VoxelShape SHAPE_NORTH = makeCuboidShape(5, 5, 0, 11, 11, 6);
    private static final VoxelShape SHAPE_WEST = makeCuboidShape(0, 5, 5, 6, 11, 11);
    private static final VoxelShape SHAPE_DOWN = makeCuboidShape(5, 0, 5, 11, 6, 11);
    private static final VoxelShape SHAPE_UP = makeCuboidShape(5, 11, 5, 11, 16, 11);
    private static final VoxelShape SHAPE_EAST = makeCuboidShape(11, 5,5, 16, 11, 11);
    private static final VoxelShape SHAPE_SOUTH = makeCuboidShape(5, 5, 11, 11, 11, 16);
    //</editor-fold>

    public LightCable(@NotNull final DyeColor color) {
        super(color, HashMap::new);

        setDefaultState(
            getStateContainer()
                .getBaseState()
                .with(NORTH, false)
                .with(SOUTH, false)
                .with(EAST, false)
                .with(WEST, false)
                .with(UP, false)
                .with(DOWN, false)
                .with(ACTIVE, true)
        );
    }

    public LightCable() {
        this(DyeColor.WHITE);
    }

    @Override
    protected void fillStateContainer(@NotNull final StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder.add(
            UP, DOWN, NORTH, SOUTH, EAST, WEST
        ));
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(
        @NotNull final BlockState state,
        @NotNull final IBlockReader world,
        @NotNull final BlockPos pos,
        @NotNull final ISelectionContext context
    ) {
        VoxelShape shape = SHAPE_CENTER;

        if (state.get(NORTH)) shape = VoxelShapes.or(shape, SHAPE_NORTH);
        if (state.get(SOUTH)) shape = VoxelShapes.or(shape, SHAPE_SOUTH);

        if (state.get(WEST)) shape = VoxelShapes.or(shape, SHAPE_WEST);
        if (state.get(EAST)) shape = VoxelShapes.or(shape, SHAPE_EAST);

        if (state.get(DOWN)) shape = VoxelShapes.or(shape, SHAPE_DOWN);
        if (state.get(UP)) shape = VoxelShapes.or(shape, SHAPE_UP);

        return shape;
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public BlockState updatePostPlacement(
        @NotNull final BlockState state,
        @NotNull final Direction facing,
        @NotNull final BlockState facingState,
        @NotNull final IWorld world,
        @NotNull final BlockPos currentPos,
        @NotNull final BlockPos facingPos
    ) {
        final boolean up = canConnectTo(world, currentPos, Direction.UP);
        final boolean down = canConnectTo(world, currentPos, Direction.DOWN);
        final boolean north = canConnectTo(world, currentPos, Direction.NORTH);
        final boolean south = canConnectTo(world, currentPos, Direction.SOUTH);
        final boolean east = canConnectTo(world, currentPos, Direction.EAST);
        final boolean west = canConnectTo(world, currentPos, Direction.WEST);

        return state
            .with(UP, up)
            .with(DOWN, down)
            .with(NORTH, north)
            .with(SOUTH, south)
            .with(EAST, east)
            .with(WEST, west);
    }

    private boolean canConnectTo(
        @NotNull final IWorld world,
        @NotNull final BlockPos pos,
        @NotNull final Direction direction
    ) {
        final BlockPos offsetPos = pos.offset(direction);
        final BlockState offsetState = world.getBlockState(offsetPos);

        return offsetState.getBlock() == this;
    }
}
