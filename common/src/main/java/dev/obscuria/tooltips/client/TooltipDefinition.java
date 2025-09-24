package dev.obscuria.tooltips.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.filter.ItemFilter;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.world.item.ItemStack;

public record TooltipDefinition(
        int priority,
        TooltipStyle style,
        ItemFilter filter
) implements Comparable<TooltipDefinition> {

    public static final Codec<TooltipDefinition> DIRECT_CODEC;

    public boolean isFor(ItemStack stack) {
        return filter.test(stack);
    }

    @Override
    public int compareTo(TooltipDefinition other) {
        return Integer.compare(priority, other.priority);
    }

    public static TooltipStyle aggregateStyleFor(ItemStack stack) {
        var style = TooltipStyle.EMPTY;
        for (var definition : TooltipsRegistries.Resource.TOOLTIP_DEFINITION.listElements()) {
            if (!definition.isFor(stack)) continue;
            style = style.merge(definition.style);
        }
        return style;
    }

    static {
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("priority").forGetter(TooltipDefinition::priority),
                TooltipStyle.CODEC.fieldOf("style").forGetter(TooltipDefinition::style),
                ItemFilter.CODEC.fieldOf("filter").forGetter(TooltipDefinition::filter)
        ).apply(codec, TooltipDefinition::new));
    }
}
