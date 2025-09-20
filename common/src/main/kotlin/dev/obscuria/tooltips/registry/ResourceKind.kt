package dev.obscuria.tooltips.registry

import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import dev.obscuria.tooltips.ObscureTooltips.LOGGER
import dev.obscuria.tooltips.client.TooltipDefinition
import dev.obscuria.tooltips.client.TooltipLabel
import dev.obscuria.tooltips.client.TooltipStyle
import dev.obscuria.tooltips.client.element.effect.ITooltipEffect
import dev.obscuria.tooltips.client.element.frame.ITooltipFrame
import dev.obscuria.tooltips.client.element.icon.ITooltipIcon
import dev.obscuria.tooltips.client.element.panel.ITooltipPanel
import dev.obscuria.tooltips.client.element.slot.ITooltipSlot
import net.minecraft.resources.ResourceLocation

enum class ResourceKind(val spec: Spec<*>) {
    PANEL(Spec("panel", "element/panel", ITooltipPanel.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_PANEL)),
    FRAME(Spec("frame", "element/frame", ITooltipFrame.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_FRAME)),
    SLOT(Spec("slot", "element/slot", ITooltipSlot.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_SLOT)),
    ICON(Spec("icon", "element/icon", ITooltipIcon.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_ICON)),
    EFFECT(Spec("effect", "element/effect", ITooltipEffect.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_EFFECT)),
    STYLES(Spec("style", "style", TooltipStyle.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_STYLE)),
    DEFINITIONS(Spec("definition", "definition", TooltipDefinition.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_DEFINITION)),
    LABEL(Spec("label", "label", TooltipLabel.DIRECT_CODEC, TooltipsRegistries.Resource.TOOLTIP_LABEL));

    data class Spec<T>(
        val name: String,
        val directory: String,
        val codec: Codec<T>,
        val registry: ResourceRegistry<T>
    ) {

        fun resourceDir(): String {
            return "tooltips/${directory}"
        }

        fun onReloadStart() {
            registry.onReloadStart()
        }

        fun load(id: ResourceLocation, element: JsonElement) {
            val result = codec.decode(JsonOps.INSTANCE, element)
            result.result().ifPresent { registry.register(id, it.first) }
            result.error().ifPresent { LOGGER.error("Failed to register $name with id $id: ${it.message()}") }
        }

        fun onReloadEnd() {
            registry.onReloadEnd()
            LOGGER.info("Loaded {} resources from {}", registry.total, resourceDir())
        }
    }
}