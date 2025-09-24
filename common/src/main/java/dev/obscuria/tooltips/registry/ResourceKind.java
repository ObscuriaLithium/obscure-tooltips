package dev.obscuria.tooltips.registry;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.TooltipDefinition;
import dev.obscuria.tooltips.client.TooltipLabel;
import dev.obscuria.tooltips.client.TooltipStyle;
import dev.obscuria.tooltips.client.element.effect.TooltipEffect;
import dev.obscuria.tooltips.client.element.frame.TooltipFrame;
import dev.obscuria.tooltips.client.element.icon.TooltipIcon;
import dev.obscuria.tooltips.client.element.panel.TooltipPanel;
import dev.obscuria.tooltips.client.element.slot.TooltipSlot;
import net.minecraft.resources.ResourceLocation;

public enum ResourceKind {

    PANEL(new Spec<>("panel", "element/panel", TooltipPanel.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_PANEL)),
    FRAME(new Spec<>("frame", "element/frame", TooltipFrame.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_FRAME)),
    SLOT(new Spec<>("slot", "element/slot", TooltipSlot.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_SLOT)),
    ICON(new Spec<>("icon", "element/icon", TooltipIcon.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_ICON)),
    EFFECT(new Spec<>("effect", "element/effect", TooltipEffect.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_EFFECT)),
    STYLE(new Spec<>("style", "style", TooltipStyle.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_STYLE)),
    DEFINITION(new Spec<>("definition", "definition", TooltipDefinition.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_DEFINITION)),
    LABEL(new Spec<>("label", "label", TooltipLabel.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_LABEL));

    public final Spec<?> spec;

    ResourceKind(Spec<?> spec) {
        this.spec = spec;
    }

    public record Spec<T>(
            String name,
            String directory,
            Codec<T> codec,
            ResourceRegistry<T> registry) {

        public String resourceDir() {
            return "tooltips/" + directory;
        }

        public void onReloadStart() {
            registry.onReloadStart();
        }

        public void load(ResourceLocation key, JsonElement element) {
            final var result = codec.decode(JsonOps.INSTANCE, element);
            result.result().ifPresent(it -> registry.register(key, it.getFirst()));
            result.error().ifPresent(it -> ObscureTooltips.LOGGER.error("Failed to register {} with key {}: {}", name, key, it.message()));
        }

        public void onReloadEnd() {
            registry.onReloadEnd();
            ObscureTooltips.LOGGER.info("Loaded {} resources from {}", registry.total(), resourceDir());
        }
    }
}
