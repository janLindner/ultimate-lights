package me.hsgminer.ultimatelights.init;

import me.hsgminer.ultimatelights.blocks.LightBlock;
import me.hsgminer.ultimatelights.blocks.LightPanel;
import me.hsgminer.ultimatelights.blocks.LightTube;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static me.hsgminer.ultimatelights.UltimateLights.ITEM_GROUP;
import static me.hsgminer.ultimatelights.UltimateLights.MOD_ID;

@SuppressWarnings("unused")
public class BlockRegistration {

    public static final Map<RegistryObject<Block>, Supplier<Block>> ENTRIES = new LinkedHashMap<>();

    public static final RegistryObject<Block> LIGHT_BLOCK = register("light_block", LightBlock::new);
    public static final Map<DyeColor, RegistryObject<Block>> LIGHT_BLOCKS = new LinkedHashMap<>();

    public static final RegistryObject<Block> LIGHT_PANEL = register("light_panel", LightPanel::new);
    public static final Map<DyeColor, RegistryObject<Block>> LIGHT_PANELS = new LinkedHashMap<>();

    public static final RegistryObject<Block> LIGHT_TUBE = register("light_tube", LightTube::new);
    public static final Map<DyeColor, RegistryObject<Block>> LIGHT_TUBES = new LinkedHashMap<>();

    @SubscribeEvent
    public void onRegisterBlocks(@NotNull RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();

        forEachColor("light_block_", (name, color) -> LIGHT_BLOCKS.put(color, register(name, () -> new LightBlock(color))));
        forEachColor("light_panel_", (name, color) -> LIGHT_PANELS.put(color, register(name, () -> new LightPanel(color))));
        forEachColor("light_tube_", (name, color) -> LIGHT_TUBES.put(color, register(name, () -> new LightPanel(color))));

        ENTRIES.forEach((object, block) -> {
            registry.register(block.get());
            object.updateReference(registry);
        });
    }

    @NotNull
    private static RegistryObject<Block> register(@NotNull final String name, @NotNull final Supplier<Block> block) {
        return register(
            name,
            block,
            b -> () -> new BlockItem(b.get(), new Item.Properties().group(ITEM_GROUP))
        );
    }

    @NotNull
    private static RegistryObject<Block> register(
        @NotNull final String name,
        @NotNull final Supplier<Block> block,
        @NotNull final Function<RegistryObject<Block>, Supplier<? extends BlockItem>> item
    ) {
        final ResourceLocation location = new ResourceLocation(MOD_ID, name);
        final RegistryObject<Block> registryObject = RegistryObject.of(location, ForgeRegistries.BLOCKS);

        ENTRIES.put(registryObject, () -> block.get().setRegistryName(name));
        ItemRegistration.BLOCK_ITEMS.add(() -> item.apply(registryObject).get().setRegistryName(location));

        return registryObject;
    }

    private static void forEachColor(final String name, BiConsumer<String, DyeColor> function) {
        for (final DyeColor color : DyeColor.values()) {
            final String objectName = name + color.name().toLowerCase();
            function.accept(objectName, color);
        }
    }
}
