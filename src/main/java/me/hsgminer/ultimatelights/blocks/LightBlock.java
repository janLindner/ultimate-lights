package me.hsgminer.ultimatelights.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

public class LightBlock extends Block {

    private final DyeColor color;

    public LightBlock(@NotNull final DyeColor color) {
        super(
            AbstractBlock.Properties
                .create(Material.REDSTONE_LIGHT, color)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)
                .hardnessAndResistance(0.5F)

       );

        this.color = color;
    }

    public LightBlock() {
        this(DyeColor.WHITE);
    }

    @Override
    public int getLightValue(final BlockState state, final IBlockReader world, final BlockPos pos) {
        return 15;
    }
}
