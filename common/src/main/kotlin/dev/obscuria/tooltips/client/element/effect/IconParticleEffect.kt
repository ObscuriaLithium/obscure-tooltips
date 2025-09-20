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
import kotlin.math.*

class IconParticleEffect(
    val particle: ITooltipParticle
) : ITooltipEffect {

    override fun codec(): Codec<IconParticleEffect> = CODEC

    override fun canApply(effect: List<ITooltipEffect>): Boolean {
        return effect.none { it is IconParticleEffect }
    }

    override fun renderIcon(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {

        var lastParticleTime = 0f
        context.forEachParticle(this) {
            it.render(graphics, context, particle, x, y)
            lastParticleTime = max(lastParticleTime, it.startTime)
        }

        if (context.timeInSeconds() - lastParticleTime < 0.15f) return
        val origin = Vec2.ZERO
        val direction = RandomUtils.nextFloat(0f, (PI * 2f).toFloat())
        val destination = Vec2(32f * sin(direction), 32f * cos(direction))
        context.addParticle(IconParticle(this, context.timeInSeconds(), origin, destination))
    }

    private class IconParticle(
        source: Any,
        startTime: Float,
        origin: Vec2,
        destination: Vec2
    ) : TooltipParticleData(source, startTime, origin, destination) {

        override fun computeProgress(context: TooltipContext): Float {
            return Easing.EASE_OUT_CUBIC.compute(super.computeProgress(context) * 0.5f)
        }

        override fun computeTranslation(context: TooltipContext, progress: Float): Vec2 {
            return super.computeTranslation(context, progress).scale(0.4f)
        }

        override fun computeScale(context: TooltipContext, progress: Float): Float {
            return Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_CUBIC, 0.2f).compute(progress * 1.2f)
        }

        override fun computeRotation(context: TooltipContext, progress: Float): Float {
            return progress * 2f
        }
    }

    companion object {

        val CODEC: Codec<IconParticleEffect> = RecordCodecBuilder.create { codec ->
            codec.group(
                ITooltipParticle.CODEC
                    .fieldOf("particle")
                    .forGetter(IconParticleEffect::particle)
            ).apply(codec, ::IconParticleEffect)
        }
    }
}