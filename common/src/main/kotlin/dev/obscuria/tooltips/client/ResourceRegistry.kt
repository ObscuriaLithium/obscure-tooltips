package dev.obscuria.tooltips.client

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import dev.obscuria.tooltips.client.style.TooltipStyle
import dev.obscuria.tooltips.client.style.effect.ITooltipEffect
import dev.obscuria.tooltips.client.style.frame.ITooltipFrame
import dev.obscuria.tooltips.client.style.icon.ITooltipIcon
import dev.obscuria.tooltips.client.style.panel.ITooltipPanel
import dev.obscuria.tooltips.client.style.slot.ITooltipSlot
import net.minecraft.resources.ResourceLocation
import java.util.Optional
import java.util.stream.Stream

sealed class ResourceRegistry<T> {

    protected val elementByKey: MutableMap<ResourceLocation, T> = mutableMapOf()
    protected val keyByElement: MutableMap<T, ResourceLocation> = mutableMapOf()

    val total: Int get() = elementByKey.size

    fun register(key: ResourceLocation, element: T) {
        elementByKey[key] = element
        keyByElement[element] = key
    }

    fun byNameCodec(): Codec<T> {
        return ResourceLocation.CODEC.flatXmap(::tryGetElement, ::tryGetKey)
    }

    open fun listElements(): Stream<T> {
        return elementByKey.values.stream()
    }

    open fun onReloadStart() {
        elementByKey.clear()
    }

    open fun onReloadEnd() {}

    private fun tryGetElement(key: ResourceLocation): DataResult<T> {
        return Optional.ofNullable(elementByKey[key])
            .map { DataResult.success(it) }
            .orElseGet { DataResult.error<T> { "Unknown registry key in ${javaClass.simpleName}: $key" } }
    }

    private fun tryGetKey(element: T): DataResult<ResourceLocation> {
        return Optional.ofNullable(keyByElement[element])
            .map { DataResult.success(it) }
            .orElseGet { DataResult.error { "Unknown registry element in ${javaClass.simpleName}: $element" } }
    }

    data object Panels : ResourceRegistry<ITooltipPanel>()

    data object Frames : ResourceRegistry<ITooltipFrame>()

    data object Slots : ResourceRegistry<ITooltipSlot>()

    data object Icons : ResourceRegistry<ITooltipIcon>()

    data object Effects : ResourceRegistry<ITooltipEffect>()

    data object Styles : ResourceRegistry<TooltipStyle>()

    data object Definitions : ResourceRegistry<TooltipDefinition>() {

        private var sortedElements: List<TooltipDefinition> = mutableListOf()

        override fun listElements(): Stream<TooltipDefinition> {
            return sortedElements.stream()
        }

        override fun onReloadEnd() {
            sortedElements = elementByKey.values.sortedByDescending { it.priority }
        }
    }
}