package dev.obscuria.obscure_tooltips.client.tooltip;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import dev.obscuria.obscure_tooltips.client.registry.TooltipRegistries;
import dev.obscuria.obscure_tooltips.client.tooltip.filter.ItemFilter;
import dev.obscuria.obscure_tooltips.client.tooltip.label.LabelProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nullable;

@Desugar
public record TooltipLabel(
        int priority,
        LabelProvider provider,
        ItemFilter filter
) implements Comparable<TooltipLabel> {

    public static TooltipLabel fromJson(JsonObject json) {
        final int priority = JsonUtils.getInt(json, "priority");
        final LabelProvider provider = LabelProvider.fromJson(JsonUtils.getJsonObject(json, "provider"));
        final ItemFilter filter = ItemFilter.fromJson(JsonUtils.getJsonObject(json, "filter"));
        return new TooltipLabel(priority, provider, filter);
    }

    public boolean isFor(ItemStack stack) {
        return filter.test(stack);
    }

    public TooltipComponent create(ItemStack stack) {
        return provider.create(stack);
    }

    @Override
    public int compareTo(TooltipLabel other) {
        return Integer.compare(priority, other.priority);
    }

    public static @Nullable TooltipLabel findFor(ItemStack stack) {
        for (var label : TooltipRegistries.TOOLTIP_LABEL.listElements()) {
            if (!label.isFor(stack)) continue;
            return label;
        }
        return null;
    }
}
