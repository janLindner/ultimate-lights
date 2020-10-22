package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;

import static me.hsgminer.ultimatelights.init.BlockRegistration.LIGHT_PANELS;

public final class LightPanel extends AbstractHorizontalFaceLightBlock {

    //<editor-fold desc="shapes">
    private static final VoxelShape SHAPE_UP = VoxelShapes.or(
        makeCuboidShape(1, 0, 1, 15, 2, 15),
        makeCuboidShape(2, 2, 2, 14, 3, 14)
    );
    private static final VoxelShape SHAPE_DOWN = VoxelShapes.or(
        makeCuboidShape(1, 16, 1, 15, 14, 15),
        makeCuboidShape(2, 14, 2, 14, 13, 14)
    );
    private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(
        makeCuboidShape(1, 1, 0, 15, 15, 2),
        makeCuboidShape(2, 2, 2, 14, 14, 3)
    );
    private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(
        makeCuboidShape(1, 1, 16, 15, 15, 14),
        makeCuboidShape(2, 2, 14, 14, 14, 13)
    );
    private static final VoxelShape SHAPE_EAST = VoxelShapes.or(
        makeCuboidShape(0, 1, 1, 2, 15, 15),
        makeCuboidShape(2, 2, 2, 3, 14, 14)
    );
    private static final VoxelShape SHAPE_WEST = VoxelShapes.or(
        makeCuboidShape(14, 1, 1, 16, 15, 15),
        makeCuboidShape(13, 2, 2, 14, 14, 14)
    );
    //</editor-fold>

    public LightPanel(@NotNull final DyeColor color) {
        super(color, () -> LIGHT_PANELS);
    }

    public LightPanel() {
        this(DyeColor.WHITE);
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(
        @NotNull final BlockState state,
        @NotNull final IBlockReader worldIn,
        @NotNull final BlockPos pos,
        @NotNull final ISelectionContext context
    ) {
        switch (getFacing(state)) {
            case UP:
                return SHAPE_UP;

            case DOWN:
                return SHAPE_DOWN;

            case SOUTH:
                return SHAPE_SOUTH;

            case NORTH:
                return SHAPE_NORTH;

            case WEST:
                return SHAPE_WEST;

            case EAST:
                return SHAPE_EAST;

            default:
                return VoxelShapes.empty();
        }
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
        final boolean up = canConnectToPanel(state, world, currentPos, Direction.UP);
        final boolean down = canConnectToPanel(state, world, currentPos, Direction.DOWN);
        final boolean north = canConnectToPanel(state, world, currentPos, Direction.NORTH);
        final boolean south = canConnectToPanel(state, world, currentPos, Direction.SOUTH);
        final boolean east = canConnectToPanel(state, world, currentPos, Direction.EAST);
        final boolean west = canConnectToPanel(state, world, currentPos, Direction.WEST);

        return state
            .with(UP, up)
            .with(DOWN, down)
            .with(NORTH, north)
            .with(SOUTH, south)
            .with(EAST, east)
            .with(WEST, west);
    }

    private boolean canConnectToPanel(
        @NotNull final BlockState state,
        @NotNull final IWorld world,
        @NotNull final BlockPos pos,
        @NotNull final Direction direction
    ) {
        final BlockPos offsetPos = pos.offset(direction);
        final BlockState offsetState = world.getBlockState(offsetPos);

        if (offsetState.getBlock() == this) {
            return state.get(FACE) == offsetState.get(FACE);
        } else {
            return false;
        }
    }
}
