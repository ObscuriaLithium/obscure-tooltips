package dev.obscuria.tooltips;

import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ObscureTooltips {

    String MOD_ID = "obscure_tooltips";
    String MOD_NAME = "Obscure Tooltips";
    Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    static ResourceLocation key(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    static void init() {
        TooltipsRegistries.init();
    }
}