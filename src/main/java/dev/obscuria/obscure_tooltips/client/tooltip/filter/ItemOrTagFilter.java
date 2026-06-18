package dev.obscuria.obscure_tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

@Desugar
public record ItemOrTagFilter(List<Item> items, List<String> oreNames) implements ItemFilter {

    public static ItemOrTagFilter fromJson(JsonObject json) {
        final List<Item> items = new ArrayList<>();
        final List<String> oreNames = new ArrayList<>();
        for (JsonElement element : JsonUtils.getJsonArray(json, "values")) {
            final String value = element.getAsString();
            if (value.startsWith("#")) {
                oreNames.add(value.substring(1));
            } else {
                final Item item = Item.REGISTRY.getObject(new ResourceLocation(value));
                if (item != null) {
                    items.add(item);
                }
            }
        }
        return new ItemOrTagFilter(items, oreNames);
    }

    @Override
    public boolean test(ItemStack stack) {
        if (items.contains(stack.getItem())) {
            return true;
        }
        if (oreNames.isEmpty()) {
            return false;
        }
        for (int oreId : OreDictionary.getOreIDs(stack)) {
            if (oreNames.contains(OreDictionary.getOreName(oreId))) {
                return true;
            }
        }
        return false;
    }
}
