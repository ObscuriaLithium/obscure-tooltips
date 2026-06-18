package dev.obscuria.obscure_tooltips.config;

import dev.obscuria.obscure_tooltips.Tags;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class ConfigSyncHandler {
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (Tags.MOD_ID.equals(event.getModID())
                && ClientConfig.configuration != null
                && ClientConfig.configuration.hasChanged()) {
            ClientConfig.configuration.save();
        }
    }
}
