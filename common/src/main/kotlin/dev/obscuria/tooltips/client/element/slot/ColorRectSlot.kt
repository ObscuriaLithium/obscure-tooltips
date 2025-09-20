package dev.obscuria.tooltips.client.element.slot

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.obscuria.fragmentum.tools.ARGB
import dev.obscuria.tooltips.client.element.QuadPalette
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawHLine
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawRect
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawVLine
import net.minecraft.client.gui.GuiGraphics

class ColorRectSlot(
    val borders: ARGB,
    val palette: QuadPalette
): ITooltipSlot {

    override fun codec(): Codec<ColorRectSlot> = CODEC

    override fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {
        graphics.drawRect(x + 1, y + 1, width - 2, height - 2, palette)
        graphics.drawHLine(x + 1, y, width - 2, borders, borders)
        graphics.drawHLine(x + 1, y + height - 1, width - 2, borders, borders)
        graphics.drawVLine(x, y + 1, height - 2, borders, borders)
        graphics.drawVLine(x + width - 1, y + 1, height - 2, borders, borders)
    }

    companion object {

        val CODEC: Codec<ColorRectSlot> = RecordCodecBuilder.create { codec ->
            codec.group(
                ARGB.CODEC
                    .fieldOf("borders")
                    .forGetter(ColorRectSlot::borders),
                QuadPalette.CODEC
                    .fieldOf("palette")
                    .forGetter(ColorRectSlot::palette)
            ).apply(codec, ::ColorRectSlot)
        }
    }
}