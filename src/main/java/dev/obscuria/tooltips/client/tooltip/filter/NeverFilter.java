package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.item.ItemStack;

@Desugar
public record NeverFilter() implements ItemFilter {

    public static final NeverFilter INSTANCE = new NeverFilter();

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }
}
