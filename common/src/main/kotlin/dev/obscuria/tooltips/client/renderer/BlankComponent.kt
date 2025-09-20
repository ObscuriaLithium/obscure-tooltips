package dev.obscuria.tooltips.client.renderer

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

object BlankComponent : ClientTooltipComponent {

    override fun getHeight(): Int = 0

    override fun getWidth(p0: Font): Int = 0
}