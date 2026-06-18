package dev.obscuria.obscure_tooltips.client.tooltip;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.registry.ResourceRegistry;
import dev.obscuria.obscure_tooltips.client.registry.TooltipRegistries;
import dev.obscuria.obscure_tooltips.client.tooltip.element.effect.TooltipEffect;
import dev.obscuria.obscure_tooltips.client.tooltip.element.frame.TooltipFrame;
import dev.obscuria.obscure_tooltips.client.tooltip.element.icon.TooltipIcon;
import dev.obscuria.obscure_tooltips.client.tooltip.element.panel.TooltipPanel;
import dev.obscuria.obscure_tooltips.client.tooltip.element.slot.TooltipSlot;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Desugar
public record TooltipStyle(
        Optional<TooltipPanel> panel,
        Optional<TooltipFrame> frame,
        Optional<TooltipSlot> slot,
        Optional<TooltipIcon> icon,
        List<TooltipEffect> effects) {

    public static final TooltipStyle EMPTY = new TooltipStyle(
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Collections.emptyList());

    public static TooltipStyle fromJson(JsonObject json) {
        return new TooltipStyle(
                resolve(json, "panel", TooltipRegistries.TOOLTIP_PANEL),
                resolve(json, "frame", TooltipRegistries.TOOLTIP_FRAME),
                resolve(json, "slot", TooltipRegistries.TOOLTIP_SLOT),
                resolve(json, "icon", TooltipRegistries.TOOLTIP_ICON),
                resolveEffects(json));
    }

    public TooltipStyle merge(TooltipStyle other) {
        return new TooltipStyle(
                panel.isPresent() ? panel : other.panel,
                frame.isPresent() ? frame : other.frame,
                slot.isPresent() ? slot : other.slot,
                icon.isPresent() ? icon : other.icon,
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

    private static <T> Optional<T> resolve(JsonObject json, String key, ResourceRegistry<T> registry) {
        if (!json.has(key)) {
            return Optional.empty();
        }
        return Optional.ofNullable(registry.get(new ResourceLocation(JsonUtils.getString(json, key))));
    }

    private static List<TooltipEffect> resolveEffects(JsonObject json) {
        if (!json.has("effects")) {
            return Collections.emptyList();
        }
        final List<TooltipEffect> effects = new ArrayList<>();
        for (JsonElement element : JsonUtils.getJsonArray(json, "effects")) {
            final TooltipEffect effect = TooltipRegistries.TOOLTIP_EFFECT.get(new ResourceLocation(element.getAsString()));
            if (effect != null) {
                effects.add(effect);
            }
        }
        return effects;
    }
}
