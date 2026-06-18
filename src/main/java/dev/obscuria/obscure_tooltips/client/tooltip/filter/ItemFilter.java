package dev.obscuria.obscure_tooltips.client.tooltip.filter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ItemFilter {
    Map<ResourceLocation, Function<JsonObject, ItemFilter>> REGISTRY = new HashMap<>();

    boolean test(ItemStack stack);

    static void register(String name, Function<JsonObject, ItemFilter> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static ItemFilter fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, ItemFilter> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown item filter type: " + type);
        }
        return factory.apply(json);
    }

    static List<ItemFilter> parseTerms(JsonObject json) {
        final JsonArray array = JsonUtils.getJsonArray(json, "terms");
        final List<ItemFilter> terms = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            terms.add(fromJson(JsonUtils.getJsonObject(element, "term")));
        }
        return terms;
    }

    static void bootstrap() {
        register("config", ConfigFilter::fromJson);
        register("always", json -> AlwaysFilter.INSTANCE);
        register("never", json -> NeverFilter.INSTANCE);
        register("all_of", AllOfFilter::fromJson);
        register("any_of", AnyOfFilter::fromJson);
        register("none_of", NoneOfFilter::fromJson);
        register("item", ItemOrTagFilter::fromJson);
        register("mod", ModFilter::fromJson);
        register("enchantment", EnchantmentFilter::fromJson);
        register("rarity", RarityFilter::fromJson);
        register("nbt", NbtFilter::fromJson);
        register("property", PropertyFilter::fromJson);
        register("id", IdentifierFilter::fromJson);
    }
}
