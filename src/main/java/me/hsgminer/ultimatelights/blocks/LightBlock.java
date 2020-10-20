package me.hsgminer.ultimatelights.blocks;

import me.hsgminer.ultimatelights.init.BlockRegistration;
import net.minecraft.item.DyeColor;
import org.jetbrains.annotations.NotNull;

public class LightBlock extends AbstractLightBlock {

    public LightBlock(@NotNull final DyeColor color) {
        super(color, () -> BlockRegistration.LIGHT_BLOCKS);
    }

    public LightBlock() {
        this(DyeColor.WHITE);
    }
}
