package dev.obscuria.tooltips.client.style.slot

import com.mojang.serialization.Codec
import net.minecraft.client.gui.GuiGraphics

object BlankSlot : ITooltipSlot {

    val CODEC: Codec<BlankSlot> = Codec.unit(BlankSlot)

    override fun codec(): Codec<BlankSlot> = CODEC

    override fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int) {}
}