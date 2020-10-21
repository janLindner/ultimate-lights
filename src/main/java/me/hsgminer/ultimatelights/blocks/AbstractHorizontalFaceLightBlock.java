package me.hsgminer.ultimatelights.blocks;

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
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

import static me.hsgminer.ultimatelights.init.SoundRegistration.SWITCH_LIGHT_OFF;
import static me.hsgminer.ultimatelights.init.SoundRegistration.SWITCH_LIGHT_ON;

public abstract class AbstractHorizontalFaceLightBlock extends HorizontalFaceBlock {

    //<editor-fold desc="properties">
    protected static final BooleanProperty UP = BooleanProperty.create("up");
    protected static final BooleanProperty DOWN = BooleanProperty.create("down");
    protected static final BooleanProperty NORTH = BooleanProperty.create("north");
    protected static final BooleanProperty SOUTH = BooleanProperty.create("south");
    protected static final BooleanProperty EAST = BooleanProperty.create("east");
    protected static final BooleanProperty WEST = BooleanProperty.create("west");
    protected static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    //</editor-fold>

    private final Supplier<Map<DyeColor, RegistryObject<Block>>> blocks;

    public AbstractHorizontalFaceLightBlock(
        @NotNull final DyeColor color,
        @NotNull final Supplier<Map<DyeColor, RegistryObject<Block>>> blocks
    ) {
        super(Properties.create(Material.REDSTONE_LIGHT, color));
        this.blocks = blocks;

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

    @Override
    public int getLightValue(final BlockState state, final IBlockReader world, final BlockPos pos) {
        return state.get(ACTIVE) ? 15 : super.getLightValue(state, world, pos);
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
        if (color == null || state.getBlock().equals(blocks.get().get(color).get())) {
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
        final BlockState newState = blocks.get().get(color).get().getDefaultState()
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
