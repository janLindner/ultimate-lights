package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFaceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

public class LightPanel extends HorizontalFaceBlock {

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
        makeCuboidShape(14,1,1,16,15,15),
        makeCuboidShape(13,2,2,14,14,14)
    );


    private final DyeColor color;

    public LightPanel(@NotNull final DyeColor color) {
        super(
            AbstractBlock.Properties
                .create(Material.REDSTONE_LIGHT, color)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .hardnessAndResistance(0.5F)

        );

        this.color = color;
    }

    public LightPanel() {
        this(DyeColor.WHITE);
    }

    @NotNull
    @Override
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
        return 15;
    }

    @Override
    protected void fillStateContainer(@NotNull final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACE, BlockStateProperties.HORIZONTAL_FACING);
    }
}
