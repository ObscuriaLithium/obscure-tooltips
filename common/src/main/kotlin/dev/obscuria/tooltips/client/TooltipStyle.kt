package dev.obscuria.tooltips.client

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.client.element.effect.ITooltipEffect
import dev.obscuria.tooltips.client.element.frame.ITooltipFrame
import dev.obscuria.tooltips.client.element.icon.ITooltipIcon
import dev.obscuria.tooltips.client.element.panel.ITooltipPanel
import dev.obscuria.tooltips.client.element.slot.ITooltipSlot
import dev.obscuria.tooltips.registry.TooltipsRegistries
import java.util.*

data class TooltipStyle(
    val panel: Optional<ITooltipPanel> = Optional.empty(),
    val frame: Optional<ITooltipFrame> = Optional.empty(),
    val slot: Optional<ITooltipSlot> = Optional.empty(),
    val icon: Optional<ITooltipIcon> = Optional.empty(),
    val effects: List<ITooltipEffect> = emptyList()
) {

    fun merge(other: TooltipStyle): TooltipStyle = TooltipStyle(
        panel = panel.or(other::panel),
        frame = frame.or(other::frame),
        slot = slot.or(other::slot),
        icon = icon.or(other::icon),
        effects = effects.merge(other.effects)
    )

    companion object {

        val EMPTY: TooltipStyle = TooltipStyle()
        val CODEC: Codec<TooltipStyle> = TooltipsRegistries.Resource.TOOLTIP_STYLE.byNameCodec()
        val DIRECT_CODEC: Codec<TooltipStyle> = RecordCodecBuilder.create { codec ->
            codec.group(
                ITooltipPanel.CODEC
                    .optionalFieldOf("panel")
                    .forGetter(TooltipStyle::panel),
                ITooltipFrame.CODEC
                    .optionalFieldOf("frame")
                    .forGetter(TooltipStyle::frame),
                ITooltipSlot.CODEC
                    .optionalFieldOf("slot")
                    .forGetter(TooltipStyle::slot),
                ITooltipIcon.CODEC
                    .optionalFieldOf("icon")
                    .forGetter(TooltipStyle::icon),
                ITooltipEffect.CODEC.listOf()
                    .fieldOf("effects")
                    .forGetter(TooltipStyle::effects)
            ).apply(codec, ::TooltipStyle)
        }

        private fun List<ITooltipEffect>.merge(other: List<ITooltipEffect>): List<ITooltipEffect> {

            if (this.isEmpty()) return other
            if (other.isEmpty()) return this

            val result = this.toMutableList()
            for (effect in other) {
                if (!effect.canApply(result)) continue
                result.add(effect)
            }
            return result
        }
    }
}