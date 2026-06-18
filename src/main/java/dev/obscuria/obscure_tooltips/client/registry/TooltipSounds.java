package dev.obscuria.obscure_tooltips.client.registry;

import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class TooltipSounds {
    public static final SoundEvent EFFECT_EPIC_POOF = create("effect.epic_poof");

    private static SoundEvent create(String name) {
        final ResourceLocation key = ObscureTooltips.resource(name);
        return new SoundEvent(key).setRegistryName(key);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(EFFECT_EPIC_POOF);
    }
}
