package dev.obscuria.tooltips.registry

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.resources.ResourceLocation
import java.util.*

open class ResourceRegistry<T> {

    internal val elementByKey: MutableMap<ResourceLocation, T> = mutableMapOf()
    internal val keyByElement: MutableMap<T, ResourceLocation> = mutableMapOf()

    val total: Int get() = elementByKey.size

    fun register(key: ResourceLocation, element: T) {
        elementByKey[key] = element
        keyByElement[element] = key
    }

    fun byNameCodec(): Codec<T> {
        return ResourceLocation.CODEC.flatXmap(::tryGetElement, ::tryGetKey)
    }

    open fun listElements(): Collection<T> {
        return elementByKey.values
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

    class Ordered<T> : ResourceRegistry<T>() where T : Comparable<T> {

        private var sortedElements: List<T> = mutableListOf()

        override fun listElements(): Collection<T> {
            return sortedElements
        }

        override fun onReloadEnd() {
            sortedElements = elementByKey.values.sortedDescending()
        }
    }
}