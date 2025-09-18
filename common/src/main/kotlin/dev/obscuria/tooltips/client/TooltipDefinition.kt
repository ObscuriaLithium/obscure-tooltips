package dev.obscuria.tooltips.client

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.client.filter.IItemFilter
import dev.obscuria.tooltips.client.style.TooltipStyle
import net.minecraft.world.item.ItemStack

class TooltipDefinition(
    val priority: Int,
    val style: TooltipStyle,
    val filter: IItemFilter
) : Comparable<TooltipDefinition> {

    fun isFor(stack: ItemStack): Boolean {
        return filter.test(stack)
    }

    override fun compareTo(other: TooltipDefinition): Int {
        return priority.compareTo(other.priority)
    }

    companion object {

        val DIRECT_CODEC: Codec<TooltipDefinition> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.INT
                    .fieldOf("priority")
                    .forGetter(TooltipDefinition::priority),
                TooltipStyle.CODEC
                    .fieldOf("style")
                    .forGetter(TooltipDefinition::style),
                IItemFilter.CODEC
                    .fieldOf("filter")
                    .forGetter(TooltipDefinition::filter)
            ).apply(codec, ::TooltipDefinition)
        }

        fun aggregateStyleFor(stack: ItemStack): TooltipStyle {
            var style = TooltipStyle.EMPTY
            for (definition in ResourceRegistry.Definitions.listElements()) {
                if (!definition.isFor(stack)) continue
                style = style.merge(definition.style)
            }
            return style
        }
    }
}