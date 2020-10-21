package me.hsgminer.ultimatelights;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UltimateLights.MOD_ID)
public class UltimateLights {

    public static final String MOD_ID = "ultimatelights";

    public static final Logger LOGGER = LogManager.getLogger();

    public static ItemGroup ITEM_GROUP = new LightsItemGroup();

    public UltimateLights() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.register(this);
    }
}
