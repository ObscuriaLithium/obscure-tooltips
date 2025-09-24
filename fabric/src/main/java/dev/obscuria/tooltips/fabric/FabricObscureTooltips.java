package dev.obscuria.tooltips.fabric;

import dev.obscuria.tooltips.ObscureTooltips;
import net.fabricmc.api.ClientModInitializer;

public class FabricObscureTooltips implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ObscureTooltips.init();
    }
}
