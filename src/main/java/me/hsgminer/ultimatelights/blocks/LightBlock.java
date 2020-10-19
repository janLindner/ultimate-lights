package me.hsgminer.ultimatelights.blocks;

import me.hsgminer.ultimatelights.init.SoundRegistration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

import static me.hsgminer.ultimatelights.init.BlockRegistration.LIGHT_BLOCKS;
import static me.hsgminer.ultimatelights.init.SoundRegistration.SWITCH_LIGHT_OFF;
import static me.hsgminer.ultimatelights.init.SoundRegistration.SWITCH_LIGHT_ON;

public class LightBlock extends Block {

    private static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public LightBlock(@NotNull final DyeColor color) {
        super(
            AbstractBlock.Properties
                .create(Material.REDSTONE_LIGHT, color)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .hardnessAndResistance(0.5F)
       );
    }

    public LightBlock() {
        this(DyeColor.WHITE);
    }

    @Override
    public int getLightValue(final BlockState state, final IBlockReader world, final BlockPos pos) {
        return state.get(ACTIVE) ? 15 : 0;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
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
        if (color == null || state.getBlock().equals(LIGHT_BLOCKS.get(color).get())) {
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
        final BlockState newState = LIGHT_BLOCKS.get(color).get().getDefaultState();
        world.setBlockState(pos, newState);

        // consume dye item
        if (((ServerPlayerEntity) player).interactionManager.getGameType() != GameType.CREATIVE) {
            heldItem.shrink(1);
        }

        return ActionResultType.SUCCESS;
    }
}
