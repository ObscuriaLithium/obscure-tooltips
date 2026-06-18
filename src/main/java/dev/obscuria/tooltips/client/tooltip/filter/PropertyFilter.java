package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;

@Desugar
public record PropertyFilter(Boolean hasFoil) implements ItemFilter {

    public static PropertyFilter fromJson(JsonObject json) {
        final Boolean hasFoil = JsonUtils.hasField(json, "has_foil")
                ? JsonUtils.getBoolean(json, "has_foil")
                : null;
        return new PropertyFilter(hasFoil);
    }

    @Override
    public boolean test(ItemStack stack) {
        return hasFoil == null || hasFoil == stack.hasEffect();
    }
}
