package dev.obscuria.tooltips.client.element.effect

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.client.element.QuadPalette
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.extension.GraphicsExtensions.color
import dev.obscuria.tooltips.extension.GraphicsExtensions.vertex
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import kotlin.math.cos
import kotlin.math.min

class RimLightEffect(
    val outerPalette: QuadPalette,
    val innerPalette: QuadPalette
) : ITooltipEffect {

    override fun codec(): Codec<RimLightEffect> = CODEC

    override fun canApply(effect: List<ITooltipEffect>): Boolean {
        return effect.none { it is RimLightEffect }
    }

    override fun renderBack(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int, width: Int, height: Int) {

        val pX = x - 3
        val pY = y - 3
        val pWidth = width + 6
        val pHeight = height + 6

        val matrix = graphics.pose().last().pose()
        val buffer = graphics.bufferSource().getBuffer(RenderType.guiOverlay())
        val scale = 0.8 + 0.4 * cos(context.timeInSeconds())
        val offset = min(pWidth, pHeight) * 0.25 * scale

        buffer.vertex(matrix, pX, pY).color(outerPalette.topLeft).endVertex()
        buffer.vertex(matrix, pX + offset, pY + offset).color(innerPalette.topLeft).endVertex()
        buffer.vertex(matrix, pX + pWidth - offset, pY + offset).color(innerPalette.topRight).endVertex()
        buffer.vertex(matrix, pX + pWidth, pY).color(outerPalette.topRight).endVertex()

        buffer.vertex(matrix, pX + offset, pY + pHeight - offset).color(innerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, pX, pY + pHeight).color(outerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, pX + pWidth, pY + pHeight).color(outerPalette.bottomRight).endVertex()
        buffer.vertex(matrix, pX + pWidth - offset, pY + pHeight - offset).color(innerPalette.bottomRight).endVertex()

        buffer.vertex(matrix, pX, pY).color(outerPalette.topLeft).endVertex()
        buffer.vertex(matrix, pX, pY + pHeight).color(outerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, pX + offset, pY + pHeight - offset).color(innerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, pX + offset, pY + offset).color(innerPalette.topLeft).endVertex()

        buffer.vertex(matrix, pX + pWidth - offset, pY + offset).color(innerPalette.topRight).endVertex()
        buffer.vertex(matrix, pX + pWidth - offset, pY + pHeight - offset).color(innerPalette.bottomRight).endVertex()
        buffer.vertex(matrix, pX + pWidth, pY + pHeight).color(outerPalette.bottomRight).endVertex()
        buffer.vertex(matrix, pX + pWidth, pY).color(outerPalette.topRight).endVertex()
    }

    companion object {

        val CODEC: Codec<RimLightEffect> = RecordCodecBuilder.create { codec ->
            codec.group(
                QuadPalette.CODEC
                    .fieldOf("outer_palette")
                    .forGetter(RimLightEffect::outerPalette),
                QuadPalette.CODEC
                    .fieldOf("inner_palette")
                    .forGetter(RimLightEffect::innerPalette)
            ).apply(codec, ::RimLightEffect)
        }
    }
}