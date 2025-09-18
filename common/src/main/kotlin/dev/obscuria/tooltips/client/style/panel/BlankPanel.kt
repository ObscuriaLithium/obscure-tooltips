package dev.obscuria.tooltips.client.style.panel

import com.mojang.serialization.Codec
import net.minecraft.client.gui.GuiGraphics

object BlankPanel : ITooltipPanel {

    val CODEC: Codec<BlankPanel> = Codec.unit(BlankPanel)

    override fun codec(): Codec<BlankPanel> = CODEC

    override fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {}
}