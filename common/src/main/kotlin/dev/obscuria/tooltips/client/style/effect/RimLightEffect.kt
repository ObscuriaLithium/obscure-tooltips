package dev.obscuria.tooltips.client.style.effect

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.client.style.QuadPalette
import dev.obscuria.tooltips.extension.GraphicsExtensions.color
import dev.obscuria.tooltips.extension.GraphicsExtensions.vertex
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType

class RimLightEffect(
    val outerPalette: QuadPalette,
    val innerPalette: QuadPalette
) : ITooltipEffect {

    override fun codec(): Codec<RimLightEffect> = CODEC

    override fun canApply(effect: List<ITooltipEffect>): Boolean {
        return effect.none { it is RimLightEffect }
    }

    override fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {

        val x = x - 3
        val y = y - 3
        val width = width + 6
        val height = height + 6

        val matrix = graphics.pose().last().pose()
        val buffer = graphics.bufferSource().getBuffer(RenderType.guiOverlay())
        val stepX = width / 4
        val stepY = height / 4

        buffer.vertex(matrix, x, y).color(outerPalette.topLeft).endVertex()
        buffer.vertex(matrix, x + stepX, y + stepY).color(innerPalette.topLeft).endVertex()
        buffer.vertex(matrix, x + width - stepX, y + stepY).color(innerPalette.topRight).endVertex()
        buffer.vertex(matrix, x + width, y).color(outerPalette.topRight).endVertex()

        buffer.vertex(matrix, x + stepX, y + height - stepY).color(innerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, x, y + height).color(outerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, x + width, y + height).color(outerPalette.bottomRight).endVertex()
        buffer.vertex(matrix, x + width - stepX, y + height - stepY).color(innerPalette.bottomRight).endVertex()

        buffer.vertex(matrix, x, y).color(outerPalette.topLeft).endVertex()
        buffer.vertex(matrix, x, y + height).color(outerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, x + stepX, y + height - stepY).color(innerPalette.bottomLeft).endVertex()
        buffer.vertex(matrix, x + stepX, y + stepY).color(innerPalette.topLeft).endVertex()

        buffer.vertex(matrix, x + width - stepX, y + stepY).color(innerPalette.topRight).endVertex()
        buffer.vertex(matrix, x + width - stepX, y + height - stepY).color(innerPalette.bottomRight).endVertex()
        buffer.vertex(matrix, x + width, y + height).color(outerPalette.bottomRight).endVertex()
        buffer.vertex(matrix, x + width, y).color(outerPalette.topRight).endVertex()
    }

    companion object {

        val CODEC: Codec<RimLightEffect> = RecordCodecBuilder.create { codec ->
            codec.group(
                QuadPalette.CODEC
                    .fieldOf("outer_palette")
                    .forGetter(RimLightEffect::outerPalette),
                QuadPalette.CODEC
                    .fieldOf("inner_palette")
                    .forGetter(RimLightEffect::innerPalette),
            ).apply(codec, ::RimLightEffect)
        }
    }
}