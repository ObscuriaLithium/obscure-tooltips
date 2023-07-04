package com.obscuria.tooltips.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.obscuria.tooltips.ObscureTooltips;
import com.obscuria.tooltips.client.style.TooltipStylePreset;
import com.obscuria.tooltips.registry.TooltipsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ResourceLoader implements ResourceManagerReloadListener {
    public static final ResourceLoader INSTANCE = new ResourceLoader();
    private static final HashMap<ResourceLocation, TooltipStylePreset> STYLES = new HashMap<>();
    private static final HashMap<Item, TooltipStylePreset> ASSOCIATIONS = new HashMap<>();
    private ResourceLoader() {}

    public static Optional<TooltipStylePreset> getStyleFor(Item item) {
        return ASSOCIATIONS.containsKey(item) ? Optional.of(ASSOCIATIONS.get(item)) : Optional.empty();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        ASSOCIATIONS.clear();
        STYLES.clear();
        manager.listPacks().forEach(pack -> {
            for (String namespace : pack.getNamespaces(PackType.CLIENT_RESOURCES)) {
                loadStyles(namespace, pack.getResource(PackType.CLIENT_RESOURCES, new ResourceLocation(namespace, "tooltips/styles.json")));
                loadAssociations(namespace, pack.getResource(PackType.CLIENT_RESOURCES, new ResourceLocation(namespace, "tooltips/associations.json")));
            }
        });
        ObscureTooltips.LOGGER.debug("Loaded {} styles and {} associations", STYLES.size(), ASSOCIATIONS.size());
    }

    private void loadAssociations(String namespace, @Nullable IoSupplier<InputStream> resource) {
        if (resource == null) return;
        try {
            final JsonObject root = JsonParser.parseReader(new BufferedReader(new InputStreamReader(resource.get(), StandardCharsets.UTF_8))).getAsJsonObject();
            if (root.has("items"))
                for (Map.Entry<String, JsonElement> entry: root.getAsJsonObject("items").entrySet())
                    associate(namespace, entry.getKey(), entry.getValue().getAsString());
            if (root.has("groups")) {
                for (JsonElement entry: root.getAsJsonArray("groups")) {
                    final String style = entry.getAsJsonObject().get("style").getAsString();
                    for (JsonElement item: entry.getAsJsonObject().getAsJsonArray("items"))
                        associate(namespace, item.getAsString(), style);
                }
            }

        } catch (Exception ignored) {}
    }

    private void associate(String namespace, String id, String style) {
        final ResourceLocation key = style.contains(":") ? ResourceLocation.of(style, ':') : new ResourceLocation(namespace, style);
        final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        if (item != null && item != Items.AIR && STYLES.containsKey(key))
            ASSOCIATIONS.put(item, STYLES.get(key));
    }

    private void loadStyles(String namespace, @Nullable IoSupplier<InputStream> resource) {
        if (resource == null) return;
        try {
            final JsonObject root = JsonParser.parseReader(new BufferedReader(new InputStreamReader(resource.get(), StandardCharsets.UTF_8))).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry: root.entrySet())
                loadStyle(namespace, entry.getKey(), entry.getValue());
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to load custom styles.json for {} namespace", namespace);
            e.printStackTrace();
        }
    }

    private void loadStyle(String namespace, String name, JsonElement element) {
        try {
            final JsonObject object = element.getAsJsonObject();
            final JsonObject panel = object.has("panel") ? object.getAsJsonObject("panel") : null;
            final JsonObject frame = object.has("frame") ? object.getAsJsonObject("frame") : null;
            final JsonObject icon = object.has("icon") ? object.getAsJsonObject("icon") : null;
            final JsonArray effects = object.has("effects") ? object.getAsJsonArray("effects") : null;
            STYLES.put(new ResourceLocation(namespace, name), new TooltipStylePreset.Builder()
                    .withPanel(panel == null ? null
                            : TooltipsRegistry.buildPanel(
                                    ResourceLocation.of(panel.get("id").getAsString(), ':'),
                                    panel.has("params") ? panel.getAsJsonObject("params") : null)
                            .orElse(null))
                    .withFrame(frame == null ? null
                            : TooltipsRegistry.buildFrame(
                                    ResourceLocation.of(frame.get("id").getAsString(), ':'),
                                    frame.has("params") ? frame.getAsJsonObject("params") : null)
                            .orElse(null))
                    .withIcon(icon == null ? null
                            : TooltipsRegistry.buildIcon(
                                    ResourceLocation.of(icon.get("id").getAsString(), ':'),
                                    icon.has("params") ? icon.getAsJsonObject("params") : null)
                            .orElse(null))
                    .withEffects(effects == null ? new ArrayList<>() : effects.asList().stream().map(e ->
                            TooltipsRegistry.buildEffect(
                                    ResourceLocation.of(e.getAsJsonObject().get("id").getAsString(), ':'),
                                            e.getAsJsonObject().has("params") ? e.getAsJsonObject().getAsJsonObject("params") : null)
                                    .orElse((a, b, c) -> {}
                    )).toList())
                    .build());
        } catch (Exception e) {
            ObscureTooltips.LOGGER.warn("Failed to load custom style [{}:{}]", namespace, name);
            e.printStackTrace();
        }
    }
}
