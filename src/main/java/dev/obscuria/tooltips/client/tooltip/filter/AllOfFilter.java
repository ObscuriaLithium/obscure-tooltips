package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.List;

@Desugar
public record AllOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static AllOfFilter fromJson(JsonObject json) {
        return new AllOfFilter(ItemFilter.parseTerms(json));
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().allMatch(term -> term.test(stack));
    }
}
