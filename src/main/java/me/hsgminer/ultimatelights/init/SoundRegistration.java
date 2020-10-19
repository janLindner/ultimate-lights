package me.hsgminer.ultimatelights.init;

import me.hsgminer.ultimatelights.UltimateLights;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = UltimateLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistration {

    private static final List<SoundEvent> SOUNDS = new ArrayList<>();

    public static final SoundEvent SWITCH_LIGHT_ON = register("block.light.activate");
    public static final SoundEvent SWITCH_LIGHT_OFF = register("block.light.deactivate");

    @NotNull
    private static SoundEvent register(@NotNull final String name) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(UltimateLights.MOD_ID, name));
        sound.setRegistryName(name);
        SOUNDS.add(sound);
        return sound;
    }

    @SubscribeEvent
    public static void registerSounds(@NotNull final RegistryEvent.Register<SoundEvent> event) {
        SOUNDS.forEach(soundEvent -> event.getRegistry().register(soundEvent));
        SOUNDS.clear();
    }
}
