package dev.obscuria.tooltips.client.element.icon

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.client.renderer.TooltipContext
import net.minecraft.client.gui.GuiGraphics

object BlankIcon : ITooltipIcon {

    val CODEC: Codec<BlankIcon> = Codec.unit(BlankIcon)

    override fun codec(): Codec<out ITooltipIcon> = CODEC

    override fun render(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {}
}