package dev.obscuria.tooltips.client.element.icon

import com.mojang.math.Axis
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.fragmentum.tools.easing.Easing
import dev.obscuria.tooltips.client.renderer.TooltipContext
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.phys.Vec3
import java.util.*

class AccentBurstIcon(
    offset: Optional<Vec3>,
    scale: Optional<Float>
) : TransformableIcon(offset, scale) {

    override fun codec(): Codec<out ITooltipIcon> = CODEC

    override fun applyScale(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {

        val time = context.timeInSeconds()
        val scale = when {
            time < 0.25f -> Easing.EASE_OUT_CUBIC.compute(time / 0.25f) * 1.75f
            time < 0.5f -> 1.75f - 0.75f * Easing.EASE_OUT_BACK.compute((time - 0.25f) / 0.25f)
            else -> 1f
        }
        graphics.pose().scale(scale, scale, scale)
    }

    override fun applyRotation(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {
        val rotation = 360f * Easing.EASE_OUT_EXPO.compute(context.timeInSeconds()).coerceIn(0f, 1f)
        graphics.pose().mulPose(Axis.YP.rotationDegrees(rotation))
    }

    companion object {

        val CODEC: Codec<AccentBurstIcon> = RecordCodecBuilder.create { codec ->
            codec.group(
                Vec3.CODEC
                    .optionalFieldOf("offset")
                    .forGetter(AccentBurstIcon::offset),
                Codec.FLOAT
                    .optionalFieldOf("scale")
                    .forGetter(AccentBurstIcon::scale)
            ).apply(codec, ::AccentBurstIcon)
        }
    }
}