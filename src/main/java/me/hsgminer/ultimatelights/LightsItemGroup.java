package me.hsgminer.ultimatelights;

import me.hsgminer.ultimatelights.init.ItemRegistration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class LightsItemGroup extends ItemGroup {

    public LightsItemGroup() {
        super(UltimateLights.MOD_ID);
    }

    @NotNull
    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistration.DIODE.get());
    }
}
