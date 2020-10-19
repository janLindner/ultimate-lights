package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

import static me.hsgminer.ultimatelights.init.BlockRegistration.LIGHT_PANELS;
import static me.hsgminer.ultimatelights.init.SoundRegistration.SWITCH_LIGHT_OFF;
import static me.hsgminer.ultimatelights.init.SoundRegistration.SWITCH_LIGHT_ON;

public class LightPanel extends HorizontalFaceBlock {

    //<editor-fold desc="properties">
    private static final BooleanProperty UP = BooleanProperty.create("up");
    private static final BooleanProperty DOWN = BooleanProperty.create("down");
    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty WEST = BooleanProperty.create("west");
    private static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    //</editor-fold>

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
        super(
            AbstractBlock.Properties
                .create(Material.REDSTONE_LIGHT, color)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .hardnessAndResistance(0.5F)

        );

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

    @Override
    public int getLightValue(final BlockState state, final IBlockReader world, final BlockPos pos) {
        return state.get(ACTIVE) ? 15 : 0;
    }

    @Override
    protected void fillStateContainer(@NotNull final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(
            BlockStateProperties.FACE, BlockStateProperties.HORIZONTAL_FACING,
            UP, DOWN, EAST, WEST, NORTH, SOUTH, ACTIVE
        );
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

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(
        @NotNull final BlockState state,
        @NotNull final World world,
        @NotNull final BlockPos pos,
        @NotNull final PlayerEntity player,
        @NotNull final Hand hand,
        @NotNull final BlockRayTraceResult hit
    ) {
        if (world.isRemote()) return ActionResultType.PASS;
        final ItemStack heldItem = player.getHeldItem(hand);

        final DyeColor color = DyeColor.getColor(heldItem);
        if (color == null || state.getBlock().equals(LIGHT_PANELS.get(color).get())) {
            if (hand == Hand.MAIN_HAND) {
                final Boolean wasActive = state.get(ACTIVE);

                final BlockState newState = state.with(ACTIVE, !wasActive);
                world.setBlockState(pos, newState);

                final SoundEvent switchSound = wasActive ? SWITCH_LIGHT_OFF : SWITCH_LIGHT_ON;
                world.playSound(null, pos, switchSound, SoundCategory.BLOCKS, 1F, 1F);

                return ActionResultType.SUCCESS;
            }

            return ActionResultType.PASS;
        }

        // place new color
        final BlockState newState = LIGHT_PANELS.get(color).get().getDefaultState()
            .with(FACE, state.get(FACE))
            .with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING))
            .with(UP, state.get(UP))
            .with(DOWN, state.get(DOWN))
            .with(EAST, state.get(EAST))
            .with(WEST, state.get(WEST))
            .with(NORTH, state.get(NORTH))
            .with(SOUTH, state.get(SOUTH));
        world.setBlockState(pos, newState);

        // consume dye item
        if (((ServerPlayerEntity) player).interactionManager.getGameType() != GameType.CREATIVE) {
            heldItem.shrink(1);
        }

        return ActionResultType.SUCCESS;
    }
}
