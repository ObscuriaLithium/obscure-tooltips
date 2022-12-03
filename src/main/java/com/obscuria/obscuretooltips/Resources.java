package com.obscuria.obscuretooltips;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.obscuria.obscureapi.classes.IClassItem;
import com.obscuria.obscuretooltips.tooltips.Override;
import com.obscuria.obscuretooltips.tooltips.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Resources implements ResourceManagerReloadListener {
    public static final Resources INSTANCE = new Resources();
    public static final Style DEFAULT_STYLE = new Style("textures/common/back.png",
            "textures/common/border.png", "textures/common/decor.png");
    public static final Override NO_OVERRIDE = new Override(0, 0);
    private final HashMap<String, Style> STYLES = new HashMap<>();
    private final HashMap<Item, Override> OVERRIDES = new HashMap<>();
    private final HashMap<String, Style> RARITIES = new HashMap<>();
    private final HashMap<String, Style> MODS = new HashMap<>();
    private Resources() { }

    @java.lang.Override
    public void onResourceManagerReload(@NotNull net.minecraft.server.packs.resources.ResourceManager resourceManager) {
        STYLES.clear(); OVERRIDES.clear(); RARITIES.clear(); MODS.clear();
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
        } catch (Exception ignored) { }
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
        } catch (Exception ignored) { }
        //Group Overrides
        try {
            for (Resource resource : resourceManager.getResources(new ResourceLocation(ObscureTooltipsMod.MODID, "groups.json"))) {
                try (InputStream inputStream = resource.getInputStream()) {
                    JsonObject rootObject = GsonHelper.parse(new InputStreamReader(inputStream), true);
                    for (Map.Entry<String, JsonElement> entry : rootObject.entrySet()) {
                        if (entry.getValue().isJsonObject()) {
                            final JsonObject object = entry.getValue().getAsJsonObject();
                            final JsonObject overrides = object.getAsJsonObject("overrides");
                            final JsonArray items = object.getAsJsonArray("items");
                            final Override override = new Override(overrides.has("xOffset") ? overrides.get("xOffset").getAsInt() : 0,
                                    overrides.has("yOffset") ? overrides.get("yOffset").getAsInt() : 0);
                            if (overrides.has("style")) override.setStyle(getStyle(overrides.get("style").getAsString()));
                            if (overrides.has("render")) override.setRender(overrides.get("render").getAsString());
                            if (overrides.has("type")) override.setType(overrides.get("type").getAsString());
                            if (overrides.has("scale")) override.setScale(overrides.get("scale").getAsFloat());
                            for (JsonElement item : items) {
                                if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(item.getAsString()))) {
                                    OVERRIDES.put(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item.getAsString())), override);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { }
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
                    if (rootObject.has("mods") && rootObject.get("mods").isJsonObject()) {
                        for (Map.Entry<String, JsonElement> entry : rootObject.get("mods").getAsJsonObject().entrySet()) {
                            MODS.put(entry.getKey(), getStyle(entry.getValue().getAsString()));
                        }
                    }
                }
            }
        } catch (Exception ignored) { }
    }

    public Style getStyle(String key) {
        return STYLES.getOrDefault(key, DEFAULT_STYLE);
    }

    public Override getOverride(Item item) {
        return OVERRIDES.getOrDefault(item, NO_OVERRIDE);
    }

    public Style getStyle(ItemStack stack, Override override) {
        if (stack.getTag() != null && stack.getTag().contains("TooltipStyle")) return getStyle(stack.getTag().getString("TooltipStyle"));
        return override.hasStyle ? override.STYLE : MODS.getOrDefault(Objects.requireNonNull(stack.getItem().getRegistryName()).getNamespace(),
                RARITIES.getOrDefault(stack.getRarity().name(), DEFAULT_STYLE));
    }

    public String getType(ItemStack stack, Override override) {
        final Item item = stack.getItem();
        if (item instanceof IClassItem classItem) return new TranslatableComponent("icon.star").getString() + "§6"
                + classItem.getObscureClass().getLabel(((IClassItem) item).getObscureType());
        if (stack.getTag() != null && stack.getTag().contains("TooltipType")) return stack.getTag().getString("TooltipType");
        String type;
        if (override.hasType) type = override.TYPE;
        else if (item instanceof ArmorItem) type = "armor";
        else if (item instanceof ShieldItem) type = "shield";
        else if (item instanceof SwordItem) type = "weapon";
        else if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof TridentItem) type = "ranged_weapon";
        else if (item instanceof TieredItem || item.getMaxDamage(stack) > 0) type = "tool";
        else if (item.isEdible()) type = "food";
        else if (item instanceof PotionItem) type = "potion";
        else if (item instanceof EnchantedBookItem) type = "magic";
        else if (item instanceof BlockItem) type = "block";
        else if (item instanceof ArrowItem) type = "ammo";
        else type = "material";
        return new TranslatableComponent("tooltip.item_type." + type).getString();
    }

    public String getRender(ItemStack stack, Override override) {
        if (stack.getTag() != null && stack.getTag().contains("TooltipRender")) return stack.getTag().getString("TooltipRender");
        return override.hasRender ? override.RENDER : (ModConfig.Client.model.get() ? "model" : "flat");
    }

    public float getScale(ItemStack stack, Override override) {
        if (stack.getTag() != null && stack.getTag().contains("TooltipScale")) return stack.getTag().getFloat("TooltipScale");
        return override.hasScale ? override.SCALE : ModConfig.Client.scale.get().floatValue();
    }
}
