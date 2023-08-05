package com.obscuria.tooltips;

import com.obscuria.tooltips.client.ResourceLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

public class ObscureTooltipsClient {
    public static void setup() {
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager)
            manager.registerReloadListener(ResourceLoader.INSTANCE);
    }
}
