package dev.obscuria.tooltips.client.renderer

import dev.obscuria.fragmentum.tools.argbOf
import dev.obscuria.tooltips.client.style.TooltipStyle
import dev.obscuria.tooltips.extension.GraphicsExtensions.drawHLine
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemStack
import org.joml.Matrix4f
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max

class HeaderComponent(
    val stack: ItemStack,
    val style: TooltipStyle,
    val title: ClientTooltipComponent,
    val label: ClientTooltipComponent
) : ClientTooltipComponent {

    override fun getHeight(): Int = 25

    override fun getWidth(font: Font): Int = 22 + max(title.getWidth(font), label.getWidth(font))

    override fun renderText(font: Font, x: Int, y: Int, matrix: Matrix4f, source: MultiBufferSource.BufferSource) {
        title.renderText(font, 22 + x, 1 + y, matrix, source)
        label.renderText(font, 22 + x, 1 + title.height + y, matrix, source)
    }

    override fun renderImage(font: Font, x: Int, y: Int, graphics: GuiGraphics) {
        style.slot.getOrNull()?.render(graphics, x, y, 20, 20)
        style.icon.getOrNull()?.render(graphics, stack, x + 2, y + 2)

        val width = getWidth(font)
        graphics.drawHLine(x, y + 22, width / 2, argbOf(0x00FFFFFF), argbOf(0x60FFFFFF))
        graphics.drawHLine(x + width / 2, y + 22, 1 + width / 2, argbOf(0x60FFFFFF), argbOf(0x00FFFFFF))
    }
}