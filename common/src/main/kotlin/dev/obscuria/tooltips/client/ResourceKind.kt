package dev.obscuria.tooltips.client

import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import dev.obscuria.tooltips.ObscureTooltips.LOGGER
import dev.obscuria.tooltips.client.style.TooltipStyle
import dev.obscuria.tooltips.client.style.effect.ITooltipEffect
import dev.obscuria.tooltips.client.style.frame.ITooltipFrame
import dev.obscuria.tooltips.client.style.icon.ITooltipIcon
import dev.obscuria.tooltips.client.style.panel.ITooltipPanel
import dev.obscuria.tooltips.client.style.slot.ITooltipSlot
import net.minecraft.resources.ResourceLocation

enum class ResourceKind(val spec: Spec<*>) {
    PANEL(Spec("panel", "element/panel", ITooltipPanel.DIRECT_CODEC, ResourceRegistry.Panels)),
    FRAME(Spec("frame", "element/frame", ITooltipFrame.DIRECT_CODEC, ResourceRegistry.Frames)),
    SLOT(Spec("slot", "element/slot", ITooltipSlot.DIRECT_CODEC, ResourceRegistry.Slots)),
    ICON(Spec("icon", "element/icon", ITooltipIcon.DIRECT_CODEC, ResourceRegistry.Icons)),
    EFFECT(Spec("effect", "element/effect", ITooltipEffect.DIRECT_CODEC, ResourceRegistry.Effects)),
    STYLES(Spec("style", "style", TooltipStyle.DIRECT_CODEC, ResourceRegistry.Styles)),
    DEFINITIONS(Spec("definition", "definition", TooltipDefinition.DIRECT_CODEC, ResourceRegistry.Definitions));

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