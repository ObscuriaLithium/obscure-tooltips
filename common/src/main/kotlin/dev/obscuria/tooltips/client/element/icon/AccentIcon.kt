package dev.obscuria.tooltips.client.element.icon

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.fragmentum.tools.easing.Easing
import dev.obscuria.tooltips.client.renderer.TooltipContext
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.phys.Vec3
import java.util.*

class AccentIcon(
    offset: Optional<Vec3>,
    scale: Optional<Float>
) : TransformableIcon(offset, scale) {

    override fun codec(): Codec<out ITooltipIcon> = CODEC

    override fun applyScale(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {

        val time = context.timeInSeconds()
        val scale = when {
            time < 0.25f -> Easing.EASE_OUT_CUBIC.compute(time / 0.25f) * 1.25f
            time < 0.5f -> 1.25f - 0.25f * Easing.EASE_OUT_CUBIC.compute((time - 0.25f) / 0.25f)
            else -> 1f
        }
        graphics.pose().scale(scale, scale, scale)
    }

    companion object {

        val CODEC: Codec<AccentIcon> = RecordCodecBuilder.create { codec ->
            codec.group(
                Vec3.CODEC
                    .optionalFieldOf("offset")
                    .forGetter(AccentIcon::offset),
                Codec.FLOAT
                    .optionalFieldOf("scale")
                    .forGetter(AccentIcon::scale)
            ).apply(codec, ::AccentIcon)
        }
    }
}