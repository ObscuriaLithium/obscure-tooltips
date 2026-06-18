package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;

import java.util.Locale;

@Desugar
public record RarityFilter(String rarity) implements ItemFilter {

    public static RarityFilter fromJson(JsonObject json) {
        return new RarityFilter(JsonUtils.getString(json, "rarity"));
    }

    @Override
    public boolean test(ItemStack stack) {
        return rarity.equals(stack.getRarity().name().toLowerCase(Locale.ROOT));
    }
}
