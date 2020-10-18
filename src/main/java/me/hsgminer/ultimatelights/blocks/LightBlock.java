package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

import static me.hsgminer.ultimatelights.init.BlockRegistration.LIGHT_BLOCKS;

public class LightBlock extends Block {

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
        return 15;
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
        final ItemStack heldItem = player.getHeldItem(hand);

        final DyeColor color = DyeColor.getColor(heldItem);
        if (world.isRemote()
            || color == null
            || state.getBlock().equals(LIGHT_BLOCKS.get(color).get())
        ) {
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
