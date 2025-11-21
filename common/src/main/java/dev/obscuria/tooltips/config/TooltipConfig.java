package dev.obscuria.tooltips.config;

import dev.obscuria.fragmentum.config.ConfigLayout;
import dev.obscuria.fragmentum.config.ConfigOptions;
import dev.obscuria.fragmentum.config.ConfigRegistry;
import dev.obscuria.tooltips.ObscureTooltips;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class TooltipConfig {

    public static final Layout client = new Layout();
    public static final List<String> itemIDsBlacklist = new ArrayList<>();
    public static final List<Item> armorPreviewWhitelist = new ArrayList<>();
    public static final List<Item> armorPreviewBlacklist = new ArrayList<>();
    public static final List<Item> toolPreviewWhitelist = new ArrayList<>();
    public static final List<Item> toolPreviewBlacklist = new ArrayList<>();

    public static void init() {
        final var fileName = "obscuria/obscure_tooltips-client.toml";
        ConfigRegistry.registerClient(ObscureTooltips.MOD_ID, fileName, client, TooltipConfig::update);
    }

    private static void update(Layout layout) {

        itemIDsBlacklist.clear();
        armorPreviewWhitelist.clear();
        armorPreviewBlacklist.clear();
        toolPreviewWhitelist.clear();
        toolPreviewBlacklist.clear();

        itemIDsBlacklist.addAll(layout.itemsBlacklist);
        layout.armorPreviewWhitelist.forEach(id -> loadItem(id, armorPreviewWhitelist::add));
        layout.armorPreviewBlacklist.forEach(id -> loadItem(id, armorPreviewBlacklist::add));
        layout.toolPreviewWhitelist.forEach(id -> loadItem(id, toolPreviewWhitelist::add));
        layout.toolPreviewBlacklist.forEach(id -> loadItem(id, toolPreviewBlacklist::add));
    }

    private static void loadItem(String id, Consumer<Item> consumer) {
        try {
            final var location = new ResourceLocation(id);
            final var resourceKey = ResourceKey.create(Registries.ITEM, location);
            final var item = BuiltInRegistries.ITEM.getOrThrow(resourceKey);
            consumer.accept(item);
        } catch (Exception exception) {
            ObscureTooltips.LOGGER.error("Config failed to load item '{}': {}", id, exception.getMessage());
        }
    }

    public static class Layout implements ConfigLayout {

        @ConfigOptions.Value("enabled")
        @ConfigOptions.Comment("Whether Obscure Tooltips should be active at all.")
        public boolean enabled = true;
        @ConfigOptions.Value("armorPreviewEnabled")
        @ConfigOptions.Comment("Whether Obscure Tooltips should display 3D armor previews.")
        public boolean armorPreviewEnabled = true;
        @ConfigOptions.Value("toolPreviewEnabled")
        @ConfigOptions.Comment("Whether Obscure Tooltips should display 3D tool/weapon previews.")
        public boolean toolPreviewEnabled = false;

        @ConfigOptions.Value("itemsBlacklist")
        @ConfigOptions.Comment("List of item IDs that will be ignored by Obscure Tooltips and will use vanilla Minecraft tooltips instead")
        public List<String> itemsBlacklist = new ArrayList<>();
        @ConfigOptions.Value("armorPreviewWhitelist")
        @ConfigOptions.Comment("List of item IDs that should always display a 3D armor preview.")
        public List<String> armorPreviewWhitelist = new ArrayList<>();
        @ConfigOptions.Value("armorPreviewBlacklist")
        @ConfigOptions.Comment("List of item IDs that should never display a 3D armor preview.")
        public List<String> armorPreviewBlacklist = new ArrayList<>();
        @ConfigOptions.Value("toolPreviewWhitelist")
        @ConfigOptions.Comment("List of item IDs that should always display a 3D tool/weapon preview.")
        public List<String> toolPreviewWhitelist = new ArrayList<>(List.of("minecraft:trident", "minecraft:bow", "minecraft:crossbow"));
        @ConfigOptions.Value("toolPreviewBlacklist")
        @ConfigOptions.Comment("List of item IDs that should never display a 3D tool/weapon preview.")
        public List<String> toolPreviewBlacklist = new ArrayList<>();
    }
}
