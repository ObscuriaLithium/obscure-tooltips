package dev.obscuria.obscure_tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.List;

@Desugar
public record AnyOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static AnyOfFilter fromJson(JsonObject json) {
        return new AnyOfFilter(ItemFilter.parseTerms(json));
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().anyMatch(term -> term.test(stack));
    }
}
