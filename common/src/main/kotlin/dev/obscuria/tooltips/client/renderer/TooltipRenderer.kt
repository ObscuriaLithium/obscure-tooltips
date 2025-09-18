package dev.obscuria.tooltips.client.renderer

import dev.obscuria.tooltips.client.TooltipDefinition
import dev.obscuria.tooltips.client.style.TooltipStyle
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

object TooltipRenderer {

    private var lastStack: ItemStack = ItemStack.EMPTY
    private var actualStack: ItemStack = ItemStack.EMPTY
    private var style: TooltipStyle = TooltipStyle.EMPTY

    fun render(
        graphics: GuiGraphics,
        font: Font,
        components: MutableList<ClientTooltipComponent>,
        mouseX: Int,
        mouseY: Int,
        positioner: ClientTooltipPositioner
    ): Boolean {

        if (actualStack.isEmpty) return false
        if (components.isEmpty()) return false

        val title = components.removeFirst()
        val label = ClientTooltipComponent.create(Component.literal("Rarity").visualOrderText)
        components.add(0, HeaderComponent(actualStack, style, title, label))

        val width = components.maxOf { it.getWidth(font) }
        val height = components.sumOf { it.height } + if (components.size == 1) -2 else 0

        val pos = positioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), mouseX, mouseY, width, height)

        graphics.pose().pushPose()
        graphics.pose().translate(0f, 0f, 400f)

        graphics.flush()
        style.panel.getOrNull()?.render(graphics, pos.x(), pos.y(), width, height)
        style.effects.forEach { it.render(graphics, pos.x(), pos.y(), width, height) }

        graphics.pose().pushPose()
        graphics.pose().translate(0f, 0f, 2f)
        style.frame.getOrNull()?.render(graphics, pos.x(), pos.y(), width, height)
        graphics.pose().popPose()

        graphics.flush()

        var componentY = pos.y()
        components.forEach {
            it.renderText(font, pos.x(), componentY, graphics.pose().last().pose(), graphics.bufferSource())
            it.renderImage(font, pos.x(), componentY, graphics)
            componentY += it.height
        }

        graphics.pose().popPose()

        lastStack = actualStack
        actualStack = ItemStack.EMPTY
        return true
    }

    fun setTooltipStack(stack: ItemStack) {
        actualStack = stack
        if (ItemStack.isSameItemSameTags(lastStack, actualStack)) return
        style = TooltipDefinition.aggregateStyleFor(stack)
    }
}