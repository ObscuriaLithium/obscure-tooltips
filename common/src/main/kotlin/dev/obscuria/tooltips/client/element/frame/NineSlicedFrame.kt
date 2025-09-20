package dev.obscuria.tooltips.client.element.frame

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

class NineSlicedFrame(
    val textureSheet: ResourceLocation
): ITooltipFrame {

    override fun codec(): Codec<out ITooltipFrame> = CODEC

    override fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {

        graphics.blit(textureSheet, x - 31, y - 31, 1f, 1f, 45, 45, 140, 140)
        graphics.blit(textureSheet, x + width - 14, y - 31, 93f, 1f, 45, 45, 140, 140)
        graphics.blit(textureSheet, x - 31, y + height - 14, 1f, 93f, 45, 45, 140, 140)
        graphics.blit(textureSheet, x + width - 14, y + height - 14, 93f, 93f, 45, 45, 140, 140)

        graphics.blit(textureSheet, x + width / 2 - 23, y - 31, 47f, 1f, 45, 45, 140, 140)
        graphics.blit(textureSheet, x + width / 2 - 23, y + height - 14, 47f, 93f, 45, 45, 140, 140)
    }

    companion object {

        val CODEC: Codec<NineSlicedFrame> = RecordCodecBuilder.create { codec ->
            codec.group(
                ResourceLocation.CODEC
                    .fieldOf("texture_sheet")
                    .forGetter(NineSlicedFrame::textureSheet)
            ).apply(codec, ::NineSlicedFrame)
        }
    }
}