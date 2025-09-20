package dev.obscuria.tooltips.client.element.effect

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.fragmentum.tools.easing.Easing
import dev.obscuria.tooltips.client.particle.ITooltipParticle
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.client.renderer.TooltipParticleData
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.phys.Vec2
import org.apache.commons.lang3.RandomUtils
import kotlin.math.atan2
import kotlin.math.max

class InwardParticleEffect(
    val particle: ITooltipParticle
) : ITooltipEffect {

    override fun codec(): Codec<InwardParticleEffect> = CODEC

    override fun canApply(effect: List<ITooltipEffect>): Boolean {
        return effect.none { it is InwardParticleEffect }
    }

    override fun renderBack(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int, width: Int, height: Int) {

        var lastParticleTime = 0f

        context.forEachParticle(this) {
            it.render(graphics, context, particle, x, y)
            lastParticleTime = max(lastParticleTime, it.startTime)
        }

        if (context.timeInSeconds() - lastParticleTime < 0.2f) return

        val edge = RandomUtils.nextInt(1, 5)
        val ratio = RandomUtils.nextFloat(0f, 1f)
        val center = Vec2(width * 0.5f, height * 0.5f)
        val origin = when (edge) {
            1 -> Vec2(width * ratio, 0f)
            2 -> Vec2(width * ratio, height.toFloat())
            3 -> Vec2(0f, height * ratio)
            4 -> Vec2(width.toFloat(), height * ratio)
            else -> Vec2.ZERO
        }

        context.addParticle(InwardParticle(this, context.timeInSeconds(), origin, center))
    }

    private class InwardParticle(
        source: Any,
        startTime: Float,
        origin: Vec2,
        destination: Vec2
    ) : TooltipParticleData(source, startTime, origin, destination) {

        override fun computeProgress(context: TooltipContext): Float {
            return Easing.EASE_OUT_CUBIC.compute(super.computeProgress(context))
        }

        override fun computeTranslation(context: TooltipContext, progress: Float): Vec2 {
            return super.computeTranslation(context, progress).scale(0.4f)
        }

        override fun computeScale(context: TooltipContext, progress: Float): Float {
            return Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_CUBIC, 0.2f).compute(progress * 1.25f)
        }

        override fun computeRotation(context: TooltipContext, progress: Float): Float {
            return atan2(destination.y - origin.y, destination.x - origin.x)
        }
    }

    companion object {

        val CODEC: Codec<InwardParticleEffect> = RecordCodecBuilder.create { codec ->
            codec.group(
                ITooltipParticle.CODEC
                    .fieldOf("particle")
                    .forGetter(InwardParticleEffect::particle)
            ).apply(codec, ::InwardParticleEffect)
        }
    }
}