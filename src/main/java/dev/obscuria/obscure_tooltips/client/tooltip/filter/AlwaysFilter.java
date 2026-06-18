package dev.obscuria.obscure_tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.item.ItemStack;

@Desugar
public record AlwaysFilter() implements ItemFilter {

    public static final AlwaysFilter INSTANCE = new AlwaysFilter();

    @Override
    public boolean test(ItemStack stack) {
        return true;
    }
}
