package dev.obscuria.tooltips.client.element.frame

import com.mojang.serialization.Codec
import net.minecraft.client.gui.GuiGraphics

object BlankFrame: ITooltipFrame {

    val CODEC: Codec<BlankFrame> = Codec.unit(BlankFrame)

    override fun codec(): Codec<out ITooltipFrame> = CODEC

    override fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {}
}