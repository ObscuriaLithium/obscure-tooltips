package dev.obscuria.tooltips.fabric;

import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.BlacklistedItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.EventFactory;

public class FabricObscureTooltips implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ObscureTooltips.init();
        ClientLifecycleEvents.CLIENT_STARTED.register(minecraft -> {BlacklistedItems.init();});
    }
}
