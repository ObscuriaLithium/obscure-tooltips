package dev.obscuria.tooltips.client.tooltip;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.registry.TooltipRegistries;
import dev.obscuria.tooltips.client.tooltip.filter.ItemFilter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

@Desugar
public record TooltipDefinition(
        int priority,
        TooltipStyle style,
        ItemFilter filter
) implements Comparable<TooltipDefinition> {
    public static TooltipDefinition fromJson(JsonObject json) {
        final int priority = JsonUtils.getInt(json, "priority");
        final TooltipStyle style = TooltipRegistries.TOOLTIP_STYLE.get(
                new ResourceLocation(JsonUtils.getString(json, "style")));
        final ItemFilter filter = ItemFilter.fromJson(JsonUtils.getJsonObject(json, "filter"));
        return new TooltipDefinition(priority, style != null ? style : TooltipStyle.EMPTY, filter);
    }

    public boolean isFor(ItemStack stack) {
        return filter.test(stack);
    }

    @Override
    public int compareTo(TooltipDefinition other) {
        return Integer.compare(priority, other.priority);
    }

    public static TooltipStyle aggregateStyleFor(ItemStack stack) {
        var style = TooltipStyle.EMPTY;
        for (var definition : TooltipRegistries.TOOLTIP_DEFINITION.listElements()) {
            if (!definition.isFor(stack)) continue;
            style = style.merge(definition.style);
        }
        return style;
    }
}
