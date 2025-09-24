package dev.obscuria.tooltips.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.element.effect.TooltipEffect;
import dev.obscuria.tooltips.client.element.frame.TooltipFrame;
import dev.obscuria.tooltips.client.element.icon.TooltipIcon;
import dev.obscuria.tooltips.client.element.panel.TooltipPanel;
import dev.obscuria.tooltips.client.element.slot.TooltipSlot;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record TooltipStyle(
        Optional<TooltipPanel> panel,
        Optional<TooltipFrame> frame,
        Optional<TooltipSlot> slot,
        Optional<TooltipIcon> icon,
        @Unmodifiable List<TooltipEffect> effects) {

    public static final TooltipStyle EMPTY;
    public static final Codec<TooltipStyle> DIRECT_CODEC;
    public static final Codec<TooltipStyle> CODEC;

    public TooltipStyle merge(TooltipStyle other) {
        return new TooltipStyle(
                panel.or(other::panel),
                frame.or(other::frame),
                slot.or(other::slot),
                icon.or(other::icon),
                mergeEffects(other.effects));
    }

    private List<TooltipEffect> mergeEffects(List<TooltipEffect> other) {
        if (effects.isEmpty()) return other;
        if (other.isEmpty()) return effects;

        final var result = new ArrayList<>(effects);
        for (var effect : other) {
            if (!effect.canApply(result)) continue;
            result.add(effect);
        }

        return result;
    }

    static {
        EMPTY = new TooltipStyle(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of());
        CODEC = TooltipsRegistries.Resource.TOOLTIP_STYLE.byNameCodec();
        DIRECT_CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TooltipPanel.CODEC.optionalFieldOf("panel").forGetter(TooltipStyle::panel),
                TooltipFrame.CODEC.optionalFieldOf("frame").forGetter(TooltipStyle::frame),
                TooltipSlot.CODEC.optionalFieldOf("slot").forGetter(TooltipStyle::slot),
                TooltipIcon.CODEC.optionalFieldOf("icon").forGetter(TooltipStyle::icon),
                TooltipEffect.CODEC.listOf().fieldOf("effects").forGetter(TooltipStyle::effects)
        ).apply(codec, TooltipStyle::new));
    }
}
