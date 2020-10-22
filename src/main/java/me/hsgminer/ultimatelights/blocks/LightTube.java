package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class LightTube extends AbstractHorizontalFaceLightBlock {

    private static final VoxelShape FLOOR_TUBE_EAST_WEST = makeCuboidShape(0, 2, 6, 16, 6, 10);
    private static final VoxelShape FLOOR_BORDER_WEST = makeCuboidShape(0, 0, 5, 1, 7, 11);
    private static final VoxelShape FLOOR_BORDER_EAST = makeCuboidShape(15, 0, 5, 16, 7, 11);
    private static final VoxelShape FLOOR_TUBE_NORTH_SOUTH = makeCuboidShape(6, 2, 0, 10, 6, 16);
    private static final VoxelShape FLOOR_BORDER_NORTH = makeCuboidShape(5, 0, 0, 11, 7, 1);
    private static final VoxelShape FLOOR_BORDER_SOUTH = makeCuboidShape(5, 0, 15, 11, 7, 16);

    public LightTube(
        @NotNull final DyeColor color
    ) {
        super(color, HashMap::new);
    }

    public LightTube() {
        this(DyeColor.WHITE);
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
        final AttachFace face = state.get(FACE);
        final Direction direction = state.get(HORIZONTAL_FACING);

        VoxelShape shape = VoxelShapes.empty();

        if (face == AttachFace.FLOOR && (direction == Direction.NORTH || direction == Direction.SOUTH)) {
            shape = VoxelShapes.or(shape, FLOOR_TUBE_EAST_WEST);

            if (!state.get(EAST)) shape = VoxelShapes.or(shape, FLOOR_BORDER_EAST);
            if (!state.get(WEST)) shape = VoxelShapes.or(shape, FLOOR_BORDER_WEST);
        }

        if (face == AttachFace.FLOOR && (direction == Direction.WEST || direction == Direction.EAST)) {
            shape = VoxelShapes.or(shape, FLOOR_TUBE_NORTH_SOUTH);

            if (!state.get(NORTH)) shape = VoxelShapes.or(shape, FLOOR_BORDER_NORTH);
            if (!state.get(SOUTH)) shape = VoxelShapes.or(shape, FLOOR_BORDER_SOUTH);
        }

        return shape;
    }

    @NotNull
    @Override
    public BlockState updatePostPlacement(
        @NotNull final BlockState state,
        @NotNull final Direction facing,
        @NotNull final BlockState facingState,
        @NotNull final IWorld world,
        @NotNull final BlockPos currentPos,
        @NotNull final BlockPos facingPos
    ) {
        final boolean east = canConnect(state, world, currentPos, Direction.EAST);
        final boolean west = canConnect(state, world, currentPos, Direction.WEST);
        final boolean north = canConnect(state, world, currentPos, Direction.NORTH);
        final boolean south = canConnect(state, world, currentPos, Direction.SOUTH);

        return state
            .with(EAST, east)
            .with(WEST, west)
            .with(NORTH, north)
            .with(SOUTH, south);
    }

    private boolean canConnect(
        @NotNull final BlockState state,
        @NotNull final IWorld world,
        @NotNull final BlockPos pos,
        @NotNull final Direction direction
    ) {
        final BlockPos offsetPos = pos.offset(direction);
        final BlockState offsetState = world.getBlockState(offsetPos);

        if (!isTube(offsetState)) return false;
        if (offsetState.get(FACE) != state.get(FACE)) return false;

        final Direction offsetFacing = offsetState.get(HORIZONTAL_FACING);

        switch (state.get(HORIZONTAL_FACING)) {
            case EAST:
            case WEST:
                return (direction == Direction.NORTH || direction == Direction.SOUTH) && (offsetFacing == Direction.EAST || offsetFacing == Direction.WEST);

            case NORTH:
            case SOUTH:
                return (direction == Direction.EAST || direction == Direction.WEST) && (offsetFacing == Direction.NORTH || offsetFacing == Direction.SOUTH);

            default:
                return false;
        }
    }

    private boolean isTube(@NotNull final BlockState state) {
        return state.getBlock() == this;
    }

    private boolean hasConnections(@NotNull final BlockState state) {
        return state.get(UP)
               || state.get(DOWN)
               || state.get(EAST)
               || state.get(WEST)
               || state.get(NORTH)
               || state.get(SOUTH);
    }
}
