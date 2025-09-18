package dev.obscuria.tooltips.client.style.panel

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.tooltips.client.style.QuadPalette
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawFrame
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawHLine
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawRect
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawVLine
import net.minecraft.client.gui.GuiGraphics

class ColorRectPanel(
    val background: QuadPalette,
    val border: QuadPalette
) : ITooltipPanel {

    override fun codec(): Codec<ColorRectPanel> = CODEC

    override fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {
        graphics.drawRect(x - 3, y - 3, width + 6, height + 6, background)
        graphics.drawFrame(x - 3, y - 3, width + 6, height + 6, border)
        graphics.drawHLine(x - 3, y - 4, width + 6, background.topLeft, background.topRight)
        graphics.drawHLine(x - 3, y + height + 3, width + 6, background.bottomLeft, background.bottomRight)
        graphics.drawVLine(x - 4, y - 3, height + 6, background.topLeft, background.bottomLeft)
        graphics.drawVLine(x + width + 3, y - 3, height + 6, background.topRight, background.bottomRight)
    }

    companion object {

        val CODEC: Codec<ColorRectPanel> = RecordCodecBuilder.create { codec ->
            codec.group(
                QuadPalette.CODEC
                    .fieldOf("background_palette")
                    .forGetter(ColorRectPanel::background),
                QuadPalette.CODEC
                    .fieldOf("border_palette")
                    .forGetter(ColorRectPanel::border),
            ).apply(codec, ::ColorRectPanel)
        }
    }
}