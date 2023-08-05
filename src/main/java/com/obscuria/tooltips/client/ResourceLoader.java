package com.obscuria.tooltips.client;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.obscuria.tooltips.ObscureTooltips;
import com.obscuria.tooltips.client.renderer.TooltipBuilder;
import com.obscuria.tooltips.client.style.StyleFilter;
import com.obscuria.tooltips.client.style.TooltipStylePreset;
import com.obscuria.tooltips.client.style.effect.TooltipEffect;
import com.obscuria.tooltips.client.style.frame.TooltipFrame;
import com.obscuria.tooltips.client.style.icon.TooltipIcon;
import com.obscuria.tooltips.client.style.panel.TooltipPanel;
import com.obscuria.tooltips.registry.TooltipsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import oshi.util.tuples.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;

public final class ResourceLoader implements ResourceManagerReloadListener {
    public static Marker LOADER = MarkerManager.getMarker("LOADER");
    public static final ResourceLoader INSTANCE = new ResourceLoader();
    private static final HashMap<ResourceLocation, TooltipPanel> PANELS = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipFrame> FRAMES = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipIcon> ICONS = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipEffect> EFFECTS = new HashMap<>();
    private static final List<ResourceLocation> PRESETS_KEYS = new ArrayList<>();
    private static final List<ResourceLocation> STYLES_KEYS = new ArrayList<>();
    private static final HashMap<ResourceLocation, Pair<StyleFilter, TooltipStylePreset>> PRESETS = new HashMap<>();
    private static final HashMap<ResourceLocation, Pair<StyleFilter, TooltipStylePreset>> STYLES = new HashMap<>();
    private ResourceLoader() {}

    public static Optional<TooltipStylePreset> getStyleFor(ItemStack stack) {
        final TooltipStylePreset.Builder builder = new TooltipStylePreset.Builder();
        for (ResourceLocation key: STYLES_KEYS) {
            final Pair<StyleFilter, TooltipStylePreset> style = STYLES.get(key);
            if (style.getA().test(stack)) {
                style.getB().getPanel().ifPresent(builder::withPanel);
                style.getB().getFrame().ifPresent(builder::withFrame);
                style.getB().getIcon().ifPresent(builder::withIcon);
                builder.withEffects(style.getB().getEffects());
                break;
            }
        }
        for (ResourceLocation key: PRESETS_KEYS){
            final Pair<StyleFilter, TooltipStylePreset> preset = PRESETS.get(key);
            if (preset.getA().test(stack)) {
                preset.getB().getPanel().ifPresent(panel -> builder.withPanel(panel, false));
                preset.getB().getFrame().ifPresent(frame -> builder.withFrame(frame, false));
                preset.getB().getIcon().ifPresent(icon -> builder.withIcon(icon, false));
                builder.withEffects(preset.getB().getEffects());
            }
        }
        return builder.isEmpty() ? Optional.empty() : Optional.of(builder.build());
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        TooltipBuilder.clear();
        clear();
        manager.listPacks().forEach(pack -> {
            for (String namespace: pack.getNamespaces(PackType.CLIENT_RESOURCES)) {
                loadElements(pack, namespace, "tooltips/panels", PANELS, TooltipsRegistry::buildPanel);
                loadElements(pack, namespace, "tooltips/frames", FRAMES, TooltipsRegistry::buildFrame);
                loadElements(pack, namespace, "tooltips/icons", ICONS, TooltipsRegistry::buildIcon);
                loadElements(pack, namespace, "tooltips/effects", EFFECTS, TooltipsRegistry::buildEffect);
                loadPresets(pack, namespace);
                loadStyles(pack, namespace);
            }
        });
        sort();
        ObscureTooltips.LOGGER.debug(LOADER, "Loaded {} Elements, {} Presets and {} Styles",
                PANELS.size() + FRAMES.size() + ICONS.size() + EFFECTS.size(), PRESETS.size(), STYLES.size());
    }

    private void clear() {
        PANELS.clear();
        FRAMES.clear();
        ICONS.clear();
        EFFECTS.clear();
        PRESETS_KEYS.clear();
        PRESETS.clear();
        STYLES_KEYS.clear();
        STYLES.clear();
    }

    private void sort() {
        final List<ResourceLocation> presets = PRESETS_KEYS.stream()
                .sorted(Comparator.comparingInt(key -> PRESETS.get(key).getA().priority)).toList();
        PRESETS_KEYS.clear();
        PRESETS_KEYS.addAll(Lists.reverse(presets));
        final List<ResourceLocation> styles = STYLES_KEYS.stream()
                .sorted(Comparator.comparingInt(key -> STYLES.get(key).getA().priority)).toList();
        STYLES_KEYS.clear();
        STYLES_KEYS.addAll(Lists.reverse(styles));
    }

    private <T> void loadElements(PackResources pack, String namespace, String path, HashMap<ResourceLocation, T> registry,
                                  BiFunction<ResourceLocation, JsonObject, Optional<T>> builder) {
        pack.listResources(PackType.CLIENT_RESOURCES, namespace, path, (location, resource) -> {
            if (location.getPath().endsWith(".json")) {
                try {
                    final ResourceLocation key = new ResourceLocation(location.toString().replace(path + "/", "").replace(".json", ""));
                    builder.apply(key, JsonParser.parseReader(new BufferedReader(new InputStreamReader(resource.get(), StandardCharsets.UTF_8))).getAsJsonObject())
                            .ifPresent(element -> registry.put(key, element));
                } catch (Exception ignored) {}
            }
        });
    }

    private void loadPresets(PackResources pack, String namespace) {
        pack.listResources(PackType.CLIENT_RESOURCES, namespace, "tooltips/presets", (location, resource) -> {
            if (location.getPath().endsWith(".json")) {
                try {
                    final ResourceLocation key = new ResourceLocation(location.toString().replace("tooltips/presets/", "").replace(".json", ""));
                    final JsonObject root = JsonParser.parseReader(new BufferedReader(new InputStreamReader(resource.get(), StandardCharsets.UTF_8))).getAsJsonObject();
                    final TooltipStylePreset style = serializeStyle(root);
                    final StyleFilter predicate = StyleFilter.fromJson(root.getAsJsonObject("filter"));
                    if (root.has("priority")) predicate.priority = root.get("priority").getAsInt();
                    PRESETS_KEYS.add(key);
                    PRESETS.put(key, new Pair<>(predicate, style));
                } catch (Exception ignored) {}
            }
        });
    }

    private void loadStyles(PackResources pack, String namespace) {
        pack.listResources(PackType.CLIENT_RESOURCES, namespace, "tooltips/styles", (location, resource) -> {
            if (location.getPath().endsWith(".json")) {
                try {
                    final ResourceLocation key = new ResourceLocation(location.toString().replace("tooltips/styles/", "").replace(".json", ""));
                    final JsonObject root = JsonParser.parseReader(new BufferedReader(new InputStreamReader(resource.get(), StandardCharsets.UTF_8))).getAsJsonObject();
                    final TooltipStylePreset style = serializeStyle(root);
                    final StyleFilter predicate = StyleFilter.fromJson(root.getAsJsonObject("filter"));
                    if (root.has("priority")) predicate.priority = root.get("priority").getAsInt();
                    STYLES_KEYS.add(key);
                    STYLES.put(key, new Pair<>(predicate, style));
                } catch (Exception ignored) {}
            }
        });
    }

    private TooltipStylePreset serializeStyle(JsonObject root) {
        return new TooltipStylePreset.Builder()
                .withPanel(root.has("panel") ? PANELS.get(new ResourceLocation(root.get("panel").getAsString())) : null)
                .withFrame(root.has("frame") ? FRAMES.get(new ResourceLocation(root.get("frame").getAsString())) : null)
                .withIcon(root.has("icon") ? ICONS.get(new ResourceLocation(root.get("icon").getAsString())) : null)
                .withEffects(root.has("effects") ? root.getAsJsonArray("effects").asList().stream().map(
                        effect -> EFFECTS.getOrDefault(new ResourceLocation(effect.getAsString()), (a, b, c) -> {
                        })).toList() : null)
                .build();
    }
}
