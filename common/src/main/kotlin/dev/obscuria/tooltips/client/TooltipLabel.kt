package dev.obscuria.tooltips.client

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.client.filter.IItemFilter
import dev.obscuria.tooltips.client.label.ILabelProvider
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.world.item.ItemStack

class TooltipLabel(
    val priority: Int,
    val provider: ILabelProvider,
    val filter: IItemFilter
) : Comparable<TooltipLabel> {

    fun isFor(stack: ItemStack): Boolean {
        return filter.test(stack)
    }

    fun bake(stack: ItemStack): ClientTooltipComponent {
        return provider.bake(stack)
    }

    override fun compareTo(other: TooltipLabel): Int {
        return priority.compareTo(other.priority)
    }

    companion object {

        val DIRECT_CODEC: Codec<TooltipLabel> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.INT
                    .fieldOf("priority")
                    .forGetter(TooltipLabel::priority),
                ILabelProvider.CODEC
                    .fieldOf("provider")
                    .forGetter(TooltipLabel::provider),
                IItemFilter.CODEC
                    .fieldOf("filter")
                    .forGetter(TooltipLabel::filter)
            ).apply(codec, ::TooltipLabel)
        }

        fun findFor(stack: ItemStack): TooltipLabel? {
            return TooltipsRegistries.Resource.TOOLTIP_LABEL.listElements().firstOrNull { it.isFor(stack) }
        }
    }
}