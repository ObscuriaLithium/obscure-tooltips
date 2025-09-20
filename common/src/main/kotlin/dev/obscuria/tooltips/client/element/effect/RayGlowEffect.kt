package dev.obscuria.tooltips.client.element.effect

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.math.Axis
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.fragmentum.tools.ARGB
import dev.obscuria.fragmentum.tools.argbOf
import dev.obscuria.fragmentum.tools.easing.Easing
import dev.obscuria.tooltips.ObscureTooltips
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.extension.GraphicsExtensions
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

class RayGlowEffect(
    val primaryColor: ARGB,
    val secondaryColor: ARGB
) : ITooltipEffect {

    override fun codec(): Codec<RayGlowEffect> = CODEC

    override fun canApply(effect: List<ITooltipEffect>): Boolean {
        return effect.none { it is RayGlowEffect }
    }

    override fun renderIcon(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {

        val time = context.timeInSeconds()
        val base = Easing.EASE_OUT_CUBIC.compute(time / 0.5f).coerceIn(0f, 1f)
        val scale = base + 0.75f * Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_OUT_CUBIC, 0.25f).compute(time)

        RenderSystem.enableBlend()
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE)

        GraphicsExtensions.setShaderColor(primaryColor)
        renderSegment(graphics, x, y, 1f * scale, 0.5f, time)

        GraphicsExtensions.setShaderColor(mix(primaryColor, secondaryColor))
        renderSegment(graphics, x, y, 0.75f * scale, -0.33f, time)

        GraphicsExtensions.setShaderColor(secondaryColor)
        renderSegment(graphics, x, y, 0.5f * scale, 0.25f, time)

        GraphicsExtensions.resetShaderColor()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableBlend()
    }

    private fun renderSegment(graphics: GuiGraphics, x: Int, y: Int, scale: Float, rotDelta: Float, timer: Float) {

        graphics.pose().pushPose()
        graphics.pose().translate(x.toFloat(), y.toFloat(), 0f)
        graphics.pose().scale(scale, scale, scale)
        graphics.pose().mulPose(Axis.ZP.rotation(rotDelta * 3f + rotDelta * timer))
        graphics.blit(TEXTURE, -32, -32, 0f, 0f, 64, 64, 64, 64)
        graphics.pose().popPose()
    }

    private fun mix(first: ARGB, second: ARGB): ARGB {
        return argbOf(
            (first.component1() + second.component1()) * 0.5f,
            (first.component2() + second.component2()) * 0.5f,
            (first.component3() + second.component3()) * 0.5f,
            (first.component4() + second.component4()) * 0.5f
        )
    }

    companion object {

        val TEXTURE: ResourceLocation = ObscureTooltips.key("textures/gui/effect/ray_glow.png")
        val CODEC: Codec<RayGlowEffect> = RecordCodecBuilder.create { codec ->
            codec.group(
                ARGB.CODEC
                    .fieldOf("primary_color")
                    .forGetter(RayGlowEffect::primaryColor),
                ARGB.CODEC
                    .fieldOf("secondary_color")
                    .forGetter(RayGlowEffect::secondaryColor),
            ).apply(codec, ::RayGlowEffect)
        }
    }
}