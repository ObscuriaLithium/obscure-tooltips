package com.obscuria.tooltips.client.style;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StyleFilter {
    public int priority = 1000;
    private final JsonObject TAG = new JsonObject();
    private final List<Item> ITEMS = new ArrayList<>();
    private final List<String> MODS = new ArrayList<>();
    private final List<String> RARITIES = new ArrayList<>();
    private final List<String> ENCHANTMENTS_ANY_MATCH = new ArrayList<>();
    private final List<String> ENCHANTMENTS_ALL_MATCH = new ArrayList<>();
    private final List<String> KEYWORDS_ANY_MATCH = new ArrayList<>();
    private final List<String> KEYWORDS_ALL_MATCH = new ArrayList<>();
    private final List<String> KEYWORDS_NONE_MATCH = new ArrayList<>();
    private StyleFilter() {}

    public static StyleFilter fromJson(JsonObject root) {
        final StyleFilter predicate = new StyleFilter();
        if (root.has("items"))
            for (JsonElement element: root.get("items").getAsJsonArray()) {
                final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString()));
                if (item != null) predicate.ITEMS.add(item);
            }
        if (root.has("mods"))
            for (JsonElement element: root.get("mods").getAsJsonArray())
                predicate.MODS.add(element.getAsString().toLowerCase());
        if (root.has("rarity"))
            predicate.RARITIES.add(root.get("rarity").getAsString().toLowerCase());
        if (root.has("rarities"))
            for (JsonElement element: root.get("rarities").getAsJsonArray())
                predicate.RARITIES.add(element.getAsString().toLowerCase());
        if (root.has("tag")) {
            final JsonObject tag = root.getAsJsonObject("tag");
            for (String key: tag.keySet())
                predicate.TAG.add(key, tag.get(key));
        }
        if (root.has("enchantments")) {
            final JsonObject enchantments = root.getAsJsonObject("enchantments");
            if (enchantments.has("any_match"))
                for (JsonElement element : enchantments.get("any_match").getAsJsonArray())
                    predicate.ENCHANTMENTS_ANY_MATCH.add(element.getAsString().toLowerCase());
            if (enchantments.has("all_match"))
                for (JsonElement element : enchantments.get("all_match").getAsJsonArray())
                    predicate.ENCHANTMENTS_ALL_MATCH.add(element.getAsString().toLowerCase());
        }
        if (root.has("keywords")) {
            final JsonObject keywords = root.getAsJsonObject("keywords");
            if (keywords.has("any_match"))
                for (JsonElement element : keywords.get("any_match").getAsJsonArray())
                    predicate.KEYWORDS_ANY_MATCH.add(element.getAsString().toLowerCase());
            if (keywords.has("all_match"))
                for (JsonElement element : keywords.get("all_match").getAsJsonArray())
                    predicate.KEYWORDS_ALL_MATCH.add(element.getAsString().toLowerCase());
            if (keywords.has("none_match"))
                for (JsonElement element : keywords.get("none_match").getAsJsonArray())
                    predicate.KEYWORDS_NONE_MATCH.add(element.getAsString().toLowerCase());
        }
        return predicate;
    }

    public boolean test(ItemStack stack) {
        if (!MODS.isEmpty() && !MODS.contains(getNamespace(stack))) return false;
        if (!RARITIES.isEmpty() && !RARITIES.contains(stack.getRarity().name().toLowerCase())) return false;
        if (TAG.size() > 0 && hasTagMismatch(stack)) return false;
        if (!ENCHANTMENTS_ANY_MATCH.isEmpty() && hasEnchantmentMismatch(stack, ENCHANTMENTS_ANY_MATCH, results -> results.stream().anyMatch(result -> result))) return false;
        if (!ENCHANTMENTS_ALL_MATCH.isEmpty() && hasEnchantmentMismatch(stack, ENCHANTMENTS_ALL_MATCH, results -> results.stream().allMatch(result -> result))) return false;
        if (!KEYWORDS_ANY_MATCH.isEmpty() && hasKeywordMismatch(stack, KEYWORDS_ANY_MATCH, results -> results.stream().anyMatch(result -> result))) return false;
        if (!KEYWORDS_ALL_MATCH.isEmpty() && hasKeywordMismatch(stack, KEYWORDS_ALL_MATCH, results -> results.stream().allMatch(result -> result))) return false;
        if (!KEYWORDS_NONE_MATCH.isEmpty() && hasKeywordMismatch(stack, KEYWORDS_NONE_MATCH, results -> results.stream().noneMatch(result -> result))) return false;
        return ITEMS.isEmpty() || ITEMS.contains(stack.getItem());
    }

    @Override
    public String toString() {
        return "StyleFilter<%s>".formatted(priority);
    }

    private boolean hasEnchantmentMismatch(ItemStack stack, List<String> enchantments, Mode mode) {
        final List<Boolean> results = new ArrayList<>();
        final Set<Enchantment> set = stack.getAllEnchantments().keySet();
        for (String key: enchantments)
            results.add(set.stream().anyMatch(enchantment -> {
                final ResourceLocation enchantmentKey = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
                return enchantmentKey != null && enchantmentKey.toString().equals(key);
            }));
        return !mode.test(results);
    }

    private boolean hasKeywordMismatch(ItemStack stack, List<String> keywords, Mode mode) {
        final List<Boolean> results = new ArrayList<>();
        for (String key: keywords) {
            if (key.equals("enchanted")) results.add(stack.isEnchanted());
            if (key.equals("enchanted_book")) results.add(stack.getItem() instanceof EnchantedBookItem);
            if (key.equals("cursed")) results.add(stack.getAllEnchantments().entrySet().stream().anyMatch(entry -> entry.getKey().isCurse()));
            if (key.equals("foil")) results.add(stack.hasFoil());
        }
        return !mode.test(results);
    }

    private boolean hasTagMismatch(ItemStack stack) {
        if (!stack.hasTag()) return true;
        try {
            for (String key : TAG.keySet()) {
                if (!stack.getOrCreateTag().contains(key)) return true;
                if (!TAG.get(key).isJsonPrimitive()) return true;
                final JsonPrimitive value = TAG.get(key).getAsJsonPrimitive();
                if (value.isString() && value.getAsString().equals(stack.getOrCreateTag().getString(key))) continue;
                if (value.isBoolean() && value.getAsBoolean() == stack.getOrCreateTag().getBoolean(key)) continue;
                if (value.isNumber()) {
                    if (value.getAsInt() == stack.getOrCreateTag().getInt(key)) continue;
                    if (value.getAsDouble() == stack.getOrCreateTag().getDouble(key)) continue;
                    if (value.getAsFloat() == stack.getOrCreateTag().getFloat(key)) continue;
                    if (value.getAsByte() == stack.getOrCreateTag().getByte(key)) continue;
                }
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    private String getNamespace(ItemStack stack) {
        final ResourceLocation key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return key != null ? key.getNamespace() : "null";
    }

    @SuppressWarnings("all")
    @FunctionalInterface
    protected interface Mode {
        boolean test(List<Boolean> results);
    }
}
