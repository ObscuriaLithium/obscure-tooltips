package com.obscuria.obscuretooltips;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.obscuria.obscuretooltips.tooltips.Override;
import com.obscuria.obscuretooltips.tooltips.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Resources implements ResourceManagerReloadListener {
    public static final Resources INSTANCE = new Resources();
    public static final Style DEFAULT_STYLE = new Style("textures/common/back.png",
            "textures/common/border.png", "textures/common/decor.png");
    public static final Override NO_OVERRIDE = new Override(0, 0);
    private final HashMap<String, Style> STYLES = new HashMap<>();
    private final HashMap<Item, Override> OVERRIDES = new HashMap<>();
    private final HashMap<String, Style> RARITIES = new HashMap<>();
    private Resources() { }

    @java.lang.Override
    public void onResourceManagerReload(@NotNull net.minecraft.server.packs.resources.ResourceManager resourceManager) {
        STYLES.clear(); OVERRIDES.clear(); RARITIES.clear();
        //Styles
        try {
            for (Resource resource : resourceManager.getResources(new ResourceLocation(ObscureTooltipsMod.MODID, "styles.json"))) {
                try (InputStream inputStream = resource.getInputStream()) {
                    JsonObject rootObject = GsonHelper.parse(new InputStreamReader(inputStream), true);
                    for (Map.Entry<String, JsonElement> entry : rootObject.entrySet()) {
                        if (entry.getValue().isJsonObject()) {
                            final JsonObject object = entry.getValue().getAsJsonObject();
                            if (object.has("background") && object.has("border") && object.has("decorations")) {
                                STYLES.put(entry.getKey(), new Style(object.get("background").getAsString(),
                                        object.get("border").getAsString(), object.get("decorations").getAsString()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) { }
        //Item Overrides
        try {
            for (Resource resource : resourceManager.getResources(new ResourceLocation(ObscureTooltipsMod.MODID, "items.json"))) {
                try (InputStream inputStream = resource.getInputStream()) {
                    JsonObject rootObject = GsonHelper.parse(new InputStreamReader(inputStream), true);
                    for (Map.Entry<String, JsonElement> entry : rootObject.entrySet()) {
                        if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(entry.getKey())) && entry.getValue().isJsonObject()) {
                            final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getKey()));
                            final JsonObject object = entry.getValue().getAsJsonObject();
                            final Override override = new Override(object.has("xOffset") ? object.get("xOffset").getAsInt() : 0,
                                    object.has("yOffset") ? object.get("yOffset").getAsInt() : 0);
                            if (object.has("style")) override.setStyle(getStyle(object.get("style").getAsString()));
                            if (object.has("render")) override.setRender(object.get("render").getAsString());
                            if (object.has("type")) override.setType(object.get("type").getAsString());
                            if (object.has("scale")) override.setScale(object.get("scale").getAsFloat());
                            OVERRIDES.put(item, override);
                        }
                    }
                }
            }
        } catch (Exception e) { }
        //Definitions
        try {
            for (Resource resource : resourceManager.getResources(new ResourceLocation(ObscureTooltipsMod.MODID, "definitions.json"))) {
                try (InputStream inputStream = resource.getInputStream()) {
                    JsonObject rootObject = GsonHelper.parse(new InputStreamReader(inputStream), true);
                    if (rootObject.has("rarities") && rootObject.get("rarities").isJsonObject()) {
                        for (Map.Entry<String, JsonElement> entry : rootObject.get("rarities").getAsJsonObject().entrySet()) {
                            RARITIES.put(entry.getKey(), getStyle(entry.getValue().getAsString()));
                            RARITIES.put(entry.getKey().toUpperCase(), getStyle(entry.getValue().getAsString()));
                        }
                    }
                }
            }
        } catch (Exception e) { }
        System.out.println(RARITIES);
    }

    public Style getStyle(String key) {
        return STYLES.getOrDefault(key, DEFAULT_STYLE);
    }

    public Style getStyle(ItemStack stack) {
        return RARITIES.getOrDefault(stack.getRarity().name(), DEFAULT_STYLE);
    }

    public Override getOverride(Item item) {
        return OVERRIDES.getOrDefault(item, NO_OVERRIDE);
    }
}
