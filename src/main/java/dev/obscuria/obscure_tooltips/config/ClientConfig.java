package dev.obscuria.obscure_tooltips.config;

import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ClientConfig {

    public static final ConfigValue<Boolean> ENABLED;
    public static final ConfigValue<Boolean> LABELS_ENABLED;
    public static final ConfigValue<Boolean> SHADOWS_ENABLED;
    public static final ConfigValue<Double> SHADOW_OPACITY;
    public static final ConfigValue<Integer> CONTENT_MARGIN;
    public static final ConfigValue<Boolean> SHOW_VIBRANT_TOOLTIPS_HINT;
    public static final ConfigValue<List<String>> IGNORED_ITEMS;
    public static final ConfigValue<Boolean> SOUNDS_ENABLED;
    public static final ConfigValue<Double> SOUND_VOLUME;
    public static final ConfigValue<Boolean> AUTO_WRAP_ENABLED;
    public static final ConfigValue<Boolean> SCROLL_ENABLED;
    public static final ConfigValue<Integer> SCROLL_SPEED;
    public static final ConfigValue<Integer> SCROLL_MARGIN;
    public static final ConfigValue<Boolean> ARMOR_PREVIEW_ENABLED;
    public static final ConfigValue<Integer> ARMOR_PREVIEW_WIDTH;
    public static final ConfigValue<List<String>> ARMOR_PREVIEW_WHITELIST;
    public static final ConfigValue<List<String>> ARMOR_PREVIEW_BLACKLIST;
    public static final ConfigValue<Boolean> TOOL_PREVIEW_ENABLED;
    public static final ConfigValue<Integer> TOOL_PREVIEW_WIDTH;
    public static final ConfigValue<List<String>> TOOL_PREVIEW_WHITELIST;
    public static final ConfigValue<List<String>> TOOL_PREVIEW_BLACKLIST;
    public static final ConfigValue<String> DEFAULT_PANEL_BACKGROUND_TOP;
    public static final ConfigValue<String> DEFAULT_PANEL_BACKGROUND_BOTTOM;
    public static final ConfigValue<String> DEFAULT_PANEL_BORDER_TOP;
    public static final ConfigValue<String> DEFAULT_PANEL_BORDER_BOTTOM;
    public static final ConfigValue<Boolean> UNCOMMON_STYLE_ENABLED;
    public static final ConfigValue<String> UNCOMMON_PANEL_BACKGROUND_TOP;
    public static final ConfigValue<String> UNCOMMON_PANEL_BACKGROUND_BOTTOM;
    public static final ConfigValue<String> UNCOMMON_PANEL_BORDER_TOP;
    public static final ConfigValue<String> UNCOMMON_PANEL_BORDER_BOTTOM;
    public static final ConfigValue<Boolean> RARE_STYLE_ENABLED;
    public static final ConfigValue<String> RARE_PANEL_BACKGROUND_TOP;
    public static final ConfigValue<String> RARE_PANEL_BACKGROUND_BOTTOM;
    public static final ConfigValue<String> RARE_PANEL_BORDER_TOP;
    public static final ConfigValue<String> RARE_PANEL_BORDER_BOTTOM;
    public static final ConfigValue<Boolean> EPIC_STYLE_ENABLED;
    public static final ConfigValue<String> EPIC_PANEL_BACKGROUND_TOP;
    public static final ConfigValue<String> EPIC_PANEL_BACKGROUND_BOTTOM;
    public static final ConfigValue<String> EPIC_PANEL_BORDER_TOP;
    public static final ConfigValue<String> EPIC_PANEL_BORDER_BOTTOM;
    public static final ConfigValue<String> EPIC_RAY_GLOW_PRIMARY;
    public static final ConfigValue<String> EPIC_RAY_GLOW_SECONDARY;
    public static final ConfigValue<String> EPIC_SHIMMER_INNER;
    public static final ConfigValue<String> EPIC_SHIMMER_OUTER;
    public static final ConfigValue<String> EPIC_SHIMMER_ACCENT;
    public static final ConfigValue<Boolean> ENCHANTED_STYLE_ENABLED;
    public static final ConfigValue<String> ENCHANTED_PARTICLE_CENTER;
    public static final ConfigValue<String> ENCHANTED_PARTICLE_EDGE;
    public static final ConfigValue<String> ENCHANTED_GLINT_PRIMARY_WAVE;
    public static final ConfigValue<String> ENCHANTED_GLINT_PRIMARY_WAVE_GLOW;
    public static final ConfigValue<String> ENCHANTED_GLINT_SECONDARY_WAVE;
    public static final ConfigValue<String> ENCHANTED_GLINT_SECONDARY_WAVE_GLOW;
    public static final ConfigValue<String> ENCHANTED_GLINT_RING;
    public static final ConfigValue<Boolean> CURSED_STYLE_ENABLED;
    public static final ConfigValue<String> CURSED_PARTICLE_CENTER;
    public static final ConfigValue<String> CURSED_PARTICLE_EDGE;
    public static final ConfigValue<String> CURSED_GLINT_PRIMARY_WAVE;
    public static final ConfigValue<String> CURSED_GLINT_PRIMARY_WAVE_GLOW;
    public static final ConfigValue<String> CURSED_GLINT_SECONDARY_WAVE;
    public static final ConfigValue<String> CURSED_GLINT_SECONDARY_WAVE_GLOW;
    public static final ConfigValue<String> CURSED_GLINT_RING;

    private static final List<String> DEFAULT_IGNORED_ITEMS =
            Collections.singletonList("quality_equipment:reforge_gui_button");
    private static final List<String> DEFAULT_TOOL_PREVIEW_WHITELIST =
            Arrays.asList("minecraft:trident", "minecraft:bow", "minecraft:crossbow");

    public static Configuration configuration;

    public static boolean isIgnored(Item item) {
        return IGNORED_ITEMS.get().contains(idOf(item));
    }

    public static boolean isInArmorPreviewWhitelist(Item item) {
        return ARMOR_PREVIEW_WHITELIST.get().contains(idOf(item));
    }

    public static boolean isInArmorPreviewBlacklist(Item item) {
        return ARMOR_PREVIEW_BLACKLIST.get().contains(idOf(item));
    }

    public static boolean isInToolPreviewWhitelist(Item item) {
        return TOOL_PREVIEW_WHITELIST.get().contains(idOf(item));
    }

    public static boolean isInToolPreviewBlacklist(Item item) {
        return TOOL_PREVIEW_BLACKLIST.get().contains(idOf(item));
    }

    private static String idOf(Item item) {
        return String.valueOf(item.getRegistryName());
    }

    public static void disableVibrantTooltipsHint() {
        if (configuration == null) {
            return;
        }
        final Property property = configuration.getCategory(Configuration.CATEGORY_GENERAL).get("showVibrantTooltipsHint");
        property.set(false);
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    static {
        final ConfigBuilder builder = new ConfigBuilder("obscuria/obscure_tooltips-client.cfg");
        configuration = builder.getConfiguration();
        ENABLED = builder.comment("Whether Obscure Tooltips should be active at all.").defineBoolean("enabled", true);
        LABELS_ENABLED = builder.comment("Whether Obscure Tooltips should display the second line in the tooltip header.").defineBoolean("labelsEnabled", true);
        SHADOWS_ENABLED = builder.comment("Whether Obscure Tooltips should display drop shadows.").defineBoolean("shadowsEnabled", true);
        SHADOW_OPACITY = builder.comment("The opacity of the drop shadows.").defineDouble("shadowOpacity", 0.3, 0.0, 1.0);
        CONTENT_MARGIN = builder.comment("The margin (in pixels) between the tooltip frame and its content.").defineInt("contentMargin", 2, 0, 16);
        SHOW_VIBRANT_TOOLTIPS_HINT = builder.comment("Whether to show a hint about enabling the built-in Vibrant Tooltips resource pack on game start.", "Automatically disabled after being shown once.").defineBoolean("showVibrantTooltipsHint", true);
        IGNORED_ITEMS = builder.comment("List of item IDs that should be ignored by Obscure Tooltips.").defineList("ignoredItems", DEFAULT_IGNORED_ITEMS, () -> "minecraft:apple");
        builder.push("Sounds");
        SOUNDS_ENABLED = builder.comment("Whether Obscure Tooltips should play sound effects.").defineBoolean("soundsEnabled", true);
        SOUND_VOLUME = builder.comment("The volume multiplier for tooltip sound effects.").defineDouble("soundVolume", 1.0, 0.0, 4.0);
        builder.pop();
        builder.push("Scrolling-and-Wrapping");
        AUTO_WRAP_ENABLED = builder.comment("Whether Obscure Tooltips should wrap lines in tooltips.").defineBoolean("autoWrapEnabled", true);
        SCROLL_ENABLED = builder.comment("Whether long tooltips should become scrollable when they exceed the screen height.").defineBoolean("scrollEnabled", true);
        SCROLL_SPEED = builder.comment("How fast the tooltip scrolls when using the mouse wheel.").defineInt("scrollSpeed", 5, 1, 64);
        SCROLL_MARGIN = builder.comment("Extra spacing (in pixels) above and below tooltip when scrolling is enabled.").defineInt("scrollMargin", 10, 0, 64);
        builder.pop();
        builder.push("Model-Preview");
        ARMOR_PREVIEW_ENABLED = builder.comment("Whether Obscure Tooltips should display 3D armor previews.").defineBoolean("armorPreviewEnabled", true);
        ARMOR_PREVIEW_WIDTH = builder.comment("Width of the 3D armor preview in pixels.").defineInt("armorPreviewWidth", 40, 16, 128);
        ARMOR_PREVIEW_WHITELIST = builder.comment("List of item IDs that should always display a 3D armor preview.").defineList("armorPreviewWhitelist", Collections.emptyList(), () -> "minecraft:diamond_helmet");
        ARMOR_PREVIEW_BLACKLIST = builder.comment("List of item IDs that should never display a 3D armor preview.").defineList("armorPreviewBlacklist", Collections.emptyList(), () -> "minecraft:diamond_helmet");
        TOOL_PREVIEW_ENABLED = builder.comment("Whether Obscure Tooltips should display 3D tool/weapon previews.").defineBoolean("toolPreviewEnabled", true);
        TOOL_PREVIEW_WIDTH = builder.comment("Width of the 3D tool/weapon preview in pixels.").defineInt("toolPreviewWidth", 30, 16, 128);
        TOOL_PREVIEW_WHITELIST = builder.comment("List of item IDs that should always display a 3D tool/weapon preview.").defineList("toolPreviewWhitelist", DEFAULT_TOOL_PREVIEW_WHITELIST, () -> "minecraft:trident");
        TOOL_PREVIEW_BLACKLIST = builder.comment("List of item IDs that should never display a 3D tool/weapon preview.").defineList("toolPreviewBlacklist", Collections.emptyList(), () -> "minecraft:trident");
        builder.pop();
        builder.push("Styles");
        builder.push("Default");
        DEFAULT_PANEL_BACKGROUND_TOP = builder.comment("Color in #AARRGGBB format.").defineString("defaultPanelBackgroundTop", "#F0100010");
        DEFAULT_PANEL_BACKGROUND_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("defaultPanelBackgroundBottom", "#F0100010");
        DEFAULT_PANEL_BORDER_TOP = builder.comment("Color in #AARRGGBB format.").defineString("defaultPanelBorderTop", "#505000FF");
        DEFAULT_PANEL_BORDER_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("defaultPanelBorderBottom", "#5028007F");
        builder.pop();
        builder.push("Uncommon-Items");
        UNCOMMON_STYLE_ENABLED = builder.comment("Whether Obscure Tooltips should display a unique style for uncommon items.").defineBoolean("uncommonStyleEnabled", true);
        UNCOMMON_PANEL_BACKGROUND_TOP = builder.comment("Color in #AARRGGBB format.").defineString("uncommonPanelBackgroundTop", "#F0100010");
        UNCOMMON_PANEL_BACKGROUND_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("uncommonPanelBackgroundBottom", "#F0100010");
        UNCOMMON_PANEL_BORDER_TOP = builder.comment("Color in #AARRGGBB format.").defineString("uncommonPanelBorderTop", "#80FF9019");
        UNCOMMON_PANEL_BORDER_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("uncommonPanelBorderBottom", "#6075350B");
        builder.pop();
        builder.push("Rare-Items");
        RARE_STYLE_ENABLED = builder.comment("Whether Obscure Tooltips should display a unique style for rare items.").defineBoolean("rareStyleEnabled", true);
        RARE_PANEL_BACKGROUND_TOP = builder.comment("Color in #AARRGGBB format.").defineString("rarePanelBackgroundTop", "#F0100010");
        RARE_PANEL_BACKGROUND_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("rarePanelBackgroundBottom", "#F0100010");
        RARE_PANEL_BORDER_TOP = builder.comment("Color in #AARRGGBB format.").defineString("rarePanelBorderTop", "#80BBBBCC");
        RARE_PANEL_BORDER_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("rarePanelBorderBottom", "#60606070");
        builder.pop();
        builder.push("Epic-Items");
        EPIC_STYLE_ENABLED = builder.comment("Whether Obscure Tooltips should display a unique style for epic items.").defineBoolean("epicStyleEnabled", true);
        EPIC_PANEL_BACKGROUND_TOP = builder.comment("Color in #AARRGGBB format.").defineString("epicPanelBackgroundTop", "#F0100010");
        EPIC_PANEL_BACKGROUND_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("epicPanelBackgroundBottom", "#F0100010");
        EPIC_PANEL_BORDER_TOP = builder.comment("Color in #AARRGGBB format.").defineString("epicPanelBorderTop", "#80FFBB00");
        EPIC_PANEL_BORDER_BOTTOM = builder.comment("Color in #AARRGGBB format.").defineString("epicPanelBorderBottom", "#60AA5000");
        EPIC_RAY_GLOW_PRIMARY = builder.comment("Color in #AARRGGBB format.").defineString("epicRayGlowPrimary", "#FFF00FFF");
        EPIC_RAY_GLOW_SECONDARY = builder.comment("Color in #AARRGGBB format.").defineString("epicRayGlowSecondary", "#FFFF5E0F");
        EPIC_SHIMMER_INNER = builder.comment("Color in #AARRGGBB format.").defineString("epicShimmerInner", "#30F00FFF");
        EPIC_SHIMMER_OUTER = builder.comment("Color in #AARRGGBB format.").defineString("epicShimmerOuter", "#00FF00FF");
        EPIC_SHIMMER_ACCENT = builder.comment("Color in #AARRGGBB format.").defineString("epicShimmerAccent", "#BBF88FFF");
        builder.pop();
        builder.push("Enchanted-Items");
        ENCHANTED_STYLE_ENABLED = builder.comment("Whether Obscure Tooltips should display a unique style for enchanted items.").defineBoolean("enchantedStyleEnabled", true);
        ENCHANTED_PARTICLE_CENTER = builder.comment("Color in #AARRGGBB format.").defineString("enchantedParticleCenter", "#80FF80FF");
        ENCHANTED_PARTICLE_EDGE = builder.comment("Color in #AARRGGBB format.").defineString("enchantedParticleEdge", "#00AA40AA");
        ENCHANTED_GLINT_PRIMARY_WAVE = builder.comment("Color in #AARRGGBB format.").defineString("enchantedGlintPrimaryWave", "#60FABF16");
        ENCHANTED_GLINT_PRIMARY_WAVE_GLOW = builder.comment("Color in #AARRGGBB format.").defineString("enchantedGlintPrimaryWaveGlow", "#40FA9F16");
        ENCHANTED_GLINT_SECONDARY_WAVE = builder.comment("Color in #AARRGGBB format.").defineString("enchantedGlintSecondaryWave", "#50D816FA");
        ENCHANTED_GLINT_SECONDARY_WAVE_GLOW = builder.comment("Color in #AARRGGBB format.").defineString("enchantedGlintSecondaryWaveGlow", "#30D816FA");
        ENCHANTED_GLINT_RING = builder.comment("Color in #AARRGGBB format.").defineString("enchantedGlintRing", "#50FFAAFF");
        builder.pop();
        builder.push("Cursed-Items");
        CURSED_STYLE_ENABLED = builder.comment("Whether Obscure Tooltips should display a unique style for cursed items.").defineBoolean("cursedStyleEnabled", true);
        CURSED_PARTICLE_CENTER = builder.comment("Color in #AARRGGBB format.").defineString("cursedParticleCenter", "#80FF2080");
        CURSED_PARTICLE_EDGE = builder.comment("Color in #AARRGGBB format.").defineString("cursedParticleEdge", "#00AA2040");
        CURSED_GLINT_PRIMARY_WAVE = builder.comment("Color in #AARRGGBB format.").defineString("cursedGlintPrimaryWave", "#60FA1693");
        CURSED_GLINT_PRIMARY_WAVE_GLOW = builder.comment("Color in #AARRGGBB format.").defineString("cursedGlintPrimaryWaveGlow", "#40FA1679");
        CURSED_GLINT_SECONDARY_WAVE = builder.comment("Color in #AARRGGBB format.").defineString("cursedGlintSecondaryWave", "#50E23131");
        CURSED_GLINT_SECONDARY_WAVE_GLOW = builder.comment("Color in #AARRGGBB format.").defineString("cursedGlintSecondaryWaveGlow", "#30E23131");
        CURSED_GLINT_RING = builder.comment("Color in #AARRGGBB format.").defineString("cursedGlintRing", "#50FFABAA");
        builder.pop();
        builder.pop();
        builder.buildClient();
    }
}
