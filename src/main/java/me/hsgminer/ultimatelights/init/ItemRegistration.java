package me.hsgminer.ultimatelights.init;

import me.hsgminer.ultimatelights.UltimateLights;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = UltimateLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistration {

    public static final List<Supplier<Item>> BLOCK_ITEMS = new ArrayList<>();

    private static final Map<RegistryObject<Item>, Supplier<Item>> ENTRIES = new LinkedHashMap<>();

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        // register block items
        BLOCK_ITEMS.stream().map(Supplier::get).forEach(registry::register);

        // register items
        ENTRIES.forEach((object, item) -> {
            registry.register(item.get());
            object.updateReference(registry);
        });
    }

    public static final RegistryObject<Item> DIODE = register("diode");

    @NotNull
    private static RegistryObject<Item> register(@NotNull final String name) {
        return register(name, () -> new Item(new Item.Properties().group(UltimateLights.ITEM_GROUP)));
    }

    @NotNull
    private static RegistryObject<Item> register(@NotNull final String name, @NotNull final Supplier<Item> item) {
        final ResourceLocation location = new ResourceLocation(UltimateLights.MOD_ID, name);
        final RegistryObject<Item> registryObject = RegistryObject.of(location, ForgeRegistries.ITEMS);

        ENTRIES.put(registryObject, () -> item.get().setRegistryName(location));

        return registryObject;
    }
}
