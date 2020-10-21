package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LightTube extends AbstractHorizontalFaceLightBlock {

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
