package dev.obscuria.tooltips.client.particle

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.fragmentum.tools.ARGB
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.client.renderer.TooltipParticleData
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawHLineOverlay
import net.minecraft.client.gui.GuiGraphics

class HorizontalLineParticle(
    val particleCenterColor: ARGB,
    val particleEdgeColor: ARGB
) : ITooltipParticle {

    override fun codec(): Codec<HorizontalLineParticle> = CODEC

    override fun render(graphics: GuiGraphics, context: TooltipContext, data: TooltipParticleData) {
        graphics.pose().pushPose()
        graphics.pose().translate(0f, -0.5f, 0f)
        graphics.drawHLineOverlay(-8, 0, 8, particleEdgeColor, particleCenterColor)
        graphics.drawHLineOverlay(0, 0, 8, particleCenterColor, particleEdgeColor)
        graphics.pose().popPose()
    }

    companion object {

        val CODEC: Codec<HorizontalLineParticle> = RecordCodecBuilder.create { codec ->
            codec.group(
                ARGB.CODEC
                    .fieldOf("particle_center_color")
                    .forGetter(HorizontalLineParticle::particleCenterColor),
                ARGB.CODEC
                    .fieldOf("particle_edge_color")
                    .forGetter(HorizontalLineParticle::particleEdgeColor)
            ).apply(codec, ::HorizontalLineParticle)
        }
    }
}