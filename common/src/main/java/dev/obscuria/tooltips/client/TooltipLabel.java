package dev.obscuria.tooltips.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.filter.ItemFilter;
import dev.obscuria.tooltips.client.label.LabelProvider;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record TooltipLabel(
        int priority,
        LabelProvider provider,
        ItemFilter filter
) implements Comparable<TooltipLabel> {

    public static final Codec<TooltipLabel> DIRECT_CODEC;

    public boolean isFor(ItemStack stack) {
        return filter.test(stack);
    }

    public ClientTooltipComponent create(ItemStack stack) {
        return provider.create(stack);
    }

    @Override
    public int compareTo(TooltipLabel other) {
        return Integer.compare(priority, other.priority);
    }

    public static @Nullable TooltipLabel findFor(ItemStack stack) {
        for (var label : TooltipsRegistries.Resource.TOOLTIP_LABEL.listElements()) {
            if (!label.isFor(stack)) continue;
            return label;
        }
        return null;
    }

    static {
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Codec.INT.fieldOf("priority").forGetter(TooltipLabel::priority),
                LabelProvider.CODEC.fieldOf("provider").forGetter(TooltipLabel::provider),
                ItemFilter.CODEC.fieldOf("filter").forGetter(TooltipLabel::filter)
        ).apply(codec, TooltipLabel::new));
    }
}
