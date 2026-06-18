package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;

import java.util.List;

@Desugar
public record NoneOfFilter(List<ItemFilter> terms) implements ItemFilter {

    public static NoneOfFilter fromJson(JsonObject json) {
        return new NoneOfFilter(ItemFilter.parseTerms(json));
    }

    @Override
    public boolean test(ItemStack stack) {
        return terms.stream().noneMatch(term -> term.test(stack));
    }
}
