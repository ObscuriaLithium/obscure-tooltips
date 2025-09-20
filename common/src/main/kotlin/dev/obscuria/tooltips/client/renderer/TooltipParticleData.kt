package dev.obscuria.tooltips.client.renderer

import com.mojang.math.Axis
import dev.obscuria.tooltips.client.particle.ITooltipParticle
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.phys.Vec2

abstract class TooltipParticleData(
    val source: Any,
    val startTime: Float,
    val origin: Vec2,
    val destination: Vec2,
    var status: ParticleStatus = ParticleStatus.ALIVE
) {

    fun render(graphics: GuiGraphics, context: TooltipContext, particle: ITooltipParticle, x: Int, y: Int) {

        val progress = computeProgress(context)

        graphics.pose().pushPose()
        val translation = computeTranslation(context, progress)
        graphics.pose().translate(x + origin.x + translation.x, y + origin.y + translation.y, 0f)
        val scale = computeScale(context, progress)
        graphics.pose().scale(scale, scale, scale)
        val rotation = computeRotation(context, progress)
        graphics.pose().mulPose(Axis.ZP.rotation(rotation))
        particle.render(graphics, context, this)
        graphics.pose().popPose()

        status = if (progress < 1f) ParticleStatus.ALIVE else ParticleStatus.EXPIRED
    }

    open fun computeProgress(context: TooltipContext): Float {
        return (context.timeInSeconds() - startTime).coerceIn(0f, 1f)
    }

    open fun computeTranslation(context: TooltipContext, progress: Float): Vec2 {
        return Vec2(
            (destination.x - origin.x) * progress,
            (destination.y - origin.y) * progress
        )
    }

    open fun computeScale(context: TooltipContext, progress: Float): Float {
        return 1f
    }

    open fun computeRotation(context: TooltipContext, progress: Float): Float {
        return 0f
    }
}