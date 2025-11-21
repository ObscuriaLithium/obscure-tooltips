package dev.obscuria.tooltips.client;

import dev.obscuria.fragmentum.Fragmentum;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.config.TooltipConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Optional;

public class BlacklistedItems {
    private static final HashSet<Item> BLACKLIST = new HashSet<Item>();

    public static void init() {
        blacklistKnownBadItems();
        blacklistConfigItems();
    }

    public static boolean isBlacklisted(Item item) {
        return BLACKLIST.contains(item);
    }

    private static void blacklistKnownBadItems() {
        ObscureTooltips.LOGGER.info("Blacklisting known bad items... (vanilla Minecraft tooltips will be used instead for these)");
        if(Fragmentum.PLATFORM.isModLoaded("quality_equipment")) {
            blacklistItem("quality_equipment:reforge_gui_button");
        }
    }

    private static void blacklistConfigItems() {
        ObscureTooltips.LOGGER.info("Blacklisting config items... (vanilla Minecraft tooltips will be used instead for these)");
        TooltipConfig.itemIDsBlacklist.forEach(BlacklistedItems::blacklistItem);
    }

    private static void blacklistItem(String fullItemID) {
        Optional<Item> item = BuiltInRegistries.ITEM.getOptional(new ResourceLocation(fullItemID));
        if (item.isPresent()) {
            ObscureTooltips.LOGGER.info("Blacklisting item: {}", fullItemID);
            BLACKLIST.add(item.get());
        } else {
            ObscureTooltips.LOGGER.error("Failed to blacklist item: {} - item not found", fullItemID);
        }
    }
}
