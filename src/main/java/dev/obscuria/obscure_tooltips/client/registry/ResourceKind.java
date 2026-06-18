package dev.obscuria.obscure_tooltips.client.registry;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.client.tooltip.TooltipDefinition;
import dev.obscuria.obscure_tooltips.client.tooltip.TooltipLabel;
import dev.obscuria.obscure_tooltips.client.tooltip.TooltipStyle;
import dev.obscuria.obscure_tooltips.client.tooltip.element.effect.TooltipEffect;
import dev.obscuria.obscure_tooltips.client.tooltip.element.frame.TooltipFrame;
import dev.obscuria.obscure_tooltips.client.tooltip.element.icon.TooltipIcon;
import dev.obscuria.obscure_tooltips.client.tooltip.element.panel.TooltipPanel;
import dev.obscuria.obscure_tooltips.client.tooltip.element.slot.TooltipSlot;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public enum ResourceKind {
    PANEL(new Spec<>("panel", "element/panel", TooltipPanel::fromJson, TooltipRegistries.TOOLTIP_PANEL)),
    FRAME(new Spec<>("frame", "element/frame", TooltipFrame::fromJson, TooltipRegistries.TOOLTIP_FRAME)),
    SLOT(new Spec<>("slot", "element/slot", TooltipSlot::fromJson, TooltipRegistries.TOOLTIP_SLOT)),
    ICON(new Spec<>("icon", "element/icon", TooltipIcon::fromJson, TooltipRegistries.TOOLTIP_ICON)),
    EFFECT(new Spec<>("effect", "element/effect", TooltipEffect::fromJson, TooltipRegistries.TOOLTIP_EFFECT)),
    STYLE(new Spec<>("style", "style", TooltipStyle::fromJson, TooltipRegistries.TOOLTIP_STYLE)),
    DEFINITION(new Spec<>("definition", "definition", TooltipDefinition::fromJson, TooltipRegistries.TOOLTIP_DEFINITION)),
    LABEL(new Spec<>("label", "label", TooltipLabel::fromJson, TooltipRegistries.TOOLTIP_LABEL));

    public final Spec<?> spec;

    ResourceKind(Spec<?> spec) {
        this.spec = spec;
    }

    @Desugar
    public record Spec<T>(
            String name,
            String directory,
            Function<JsonObject, T> parser,
            ResourceRegistry<T> registry) {

        public String resourceDir() {
            return "tooltips/" + directory;
        }

        public void onReloadStart() {
            registry.onReloadStart();
        }

        public void load(ResourceLocation key, JsonObject json) {
            try {
                registry.register(key, parser.apply(json));
            } catch (Exception exception) {
                ObscureTooltips.LOGGER.error("Failed to register {} with key {}: {}", name, key, exception.getMessage());
            }
        }

        public void onReloadEnd() {
            registry.onReloadEnd();
            ObscureTooltips.LOGGER.info("Loaded {} resources from {}", registry.total(), resourceDir());
        }
    }
}
