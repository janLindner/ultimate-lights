package me.hsgminer.ultimatelights.init;

import me.hsgminer.ultimatelights.blocks.LightBlock;
import me.hsgminer.ultimatelights.blocks.LightPanel;
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
import java.util.function.Function;
import java.util.function.Supplier;

import static me.hsgminer.ultimatelights.UltimateLights.ITEM_GROUP;
import static me.hsgminer.ultimatelights.UltimateLights.MOD_ID;

@SuppressWarnings("unused")
public class BlockRegistration {

    public static final Map<RegistryObject<Block>, Supplier<Block>> ENTRIES = new LinkedHashMap<>();

    public static final RegistryObject<Block> LIGHT_BLOCK = register("light_block", LightBlock::new);
    public static final RegistryObject<Block> LIGHT_BLOCK_WHITE = register("light_block_white", () -> new LightBlock(DyeColor.WHITE));
    public static final RegistryObject<Block> LIGHT_BLOCK_BLACK = register("light_block_black", () -> new LightBlock(DyeColor.BLACK));
    public static final RegistryObject<Block> LIGHT_BLOCK_GRAY = register("light_block_gray", () -> new LightBlock(DyeColor.GRAY));
    public static final RegistryObject<Block> LIGHT_BLOCK_LIGHT_GRAY = register("light_block_light_gray", () -> new LightBlock(DyeColor.LIGHT_GRAY));
    public static final RegistryObject<Block> LIGHT_BLOCK_BLUE = register("light_block_blue", () -> new LightBlock(DyeColor.BLUE));
    public static final RegistryObject<Block> LIGHT_BLOCK_LIGHT_BLUE = register("light_block_light_blue", () -> new LightBlock(DyeColor.LIGHT_BLUE));
    public static final RegistryObject<Block> LIGHT_BLOCK_RED = register("light_block_red", () -> new LightBlock(DyeColor.RED));
    public static final RegistryObject<Block> LIGHT_BLOCK_ORANGE = register("light_block_orange", () -> new LightBlock(DyeColor.ORANGE));
    public static final RegistryObject<Block> LIGHT_BLOCK_YELLOW = register("light_block_yellow", () -> new LightBlock(DyeColor.YELLOW));
    public static final RegistryObject<Block> LIGHT_BLOCK_LIME = register("light_block_lime", () -> new LightBlock(DyeColor.LIME));
    public static final RegistryObject<Block> LIGHT_BLOCK_GREEN = register("light_block_green", () -> new LightBlock(DyeColor.GREEN));
    public static final RegistryObject<Block> LIGHT_BLOCK_PINK = register("light_block_pink", () -> new LightBlock(DyeColor.PINK));
    public static final RegistryObject<Block> LIGHT_BLOCK_MAGENTA = register("light_block_magenta", () -> new LightBlock(DyeColor.MAGENTA));
    public static final RegistryObject<Block> LIGHT_BLOCK_PURPLE = register("light_block_purple", () -> new LightBlock(DyeColor.PURPLE));
    public static final RegistryObject<Block> LIGHT_BLOCK_BROWN = register("light_block_brown", () -> new LightBlock(DyeColor.BROWN));
    public static final RegistryObject<Block> LIGHT_BLOCK_CYAN = register("light_block_cyan", () -> new LightBlock(DyeColor.CYAN));

    public static final RegistryObject<Block> LIGHT_PANEL = register("light_panel", LightPanel::new);
    public static final RegistryObject<Block> LIGHT_PANEL_WHITE = register("light_panel_white", () -> new LightPanel(DyeColor.WHITE));
    public static final RegistryObject<Block> LIGHT_PANEL_BLACK = register("light_panel_black", () -> new LightPanel(DyeColor.BLACK));
    public static final RegistryObject<Block> LIGHT_PANEL_GRAY = register("light_panel_gray", () -> new LightPanel(DyeColor.GRAY));
    public static final RegistryObject<Block> LIGHT_PANEL_LIGHT_GRAY = register("light_panel_light_gray", () -> new LightPanel(DyeColor.LIGHT_GRAY));
    public static final RegistryObject<Block> LIGHT_PANEL_BLUE = register("light_panel_blue", () -> new LightPanel(DyeColor.BLUE));
    public static final RegistryObject<Block> LIGHT_PANEL_LIGHT_BLUE = register("light_panel_light_blue", () -> new LightPanel(DyeColor.LIGHT_BLUE));
    public static final RegistryObject<Block> LIGHT_PANEL_RED = register("light_panel_red", () -> new LightPanel(DyeColor.RED));
    public static final RegistryObject<Block> LIGHT_PANEL_ORANGE = register("light_panel_orange", () -> new LightPanel(DyeColor.ORANGE));
    public static final RegistryObject<Block> LIGHT_PANEL_YELLOW = register("light_panel_yellow", () -> new LightPanel(DyeColor.YELLOW));
    public static final RegistryObject<Block> LIGHT_PANEL_LIME = register("light_panel_lime", () -> new LightPanel(DyeColor.LIME));
    public static final RegistryObject<Block> LIGHT_PANEL_GREEN = register("light_panel_green", () -> new LightPanel(DyeColor.GREEN));
    public static final RegistryObject<Block> LIGHT_PANEL_PINK = register("light_panel_pink", () -> new LightPanel(DyeColor.PINK));
    public static final RegistryObject<Block> LIGHT_PANEL_MAGENTA = register("light_panel_magenta", () -> new LightPanel(DyeColor.MAGENTA));
    public static final RegistryObject<Block> LIGHT_PANEL_PURPLE = register("light_panel_purple", () -> new LightPanel(DyeColor.PURPLE));
    public static final RegistryObject<Block> LIGHT_PANEL_BROWN = register("light_panel_brown", () -> new LightPanel(DyeColor.BROWN));
    public static final RegistryObject<Block> LIGHT_PANEL_CYAN = register("light_panel_cyan", () -> new LightPanel(DyeColor.CYAN));

    @SubscribeEvent
    public void onRegisterBlocks(@NotNull RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();

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
}
