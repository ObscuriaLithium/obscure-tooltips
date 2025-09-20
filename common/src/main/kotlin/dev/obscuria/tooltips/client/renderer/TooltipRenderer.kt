package dev.obscuria.tooltips.client.renderer

import dev.obscuria.tooltips.client.TooltipDefinition
import dev.obscuria.tooltips.client.TooltipLabel
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

object TooltipRenderer {

    private var lastStack: ItemStack = ItemStack.EMPTY
    private var actualStack: ItemStack = ItemStack.EMPTY
    private var context: TooltipContext = TooltipContext()

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
        val label = context.label?.bake(actualStack) ?: BlankComponent
        val minWidth = components.maxOf { it.getWidth(font) }
        components.add(0, HeaderComponent(minWidth, context, title, label))

        val width = components.maxOf { it.getWidth(font) }
        val height = components.sumOf { it.height } - 2

        val pos = positioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), mouseX, mouseY, width, height)

        graphics.pose().pushPose()
        graphics.pose().translate(0f, 0f, 400f)

        graphics.flush()
        context.style.panel.getOrNull()?.render(graphics, pos.x(), pos.y(), width, height)
        context.style.effects.forEach { it.renderBack(graphics, context, pos.x(), pos.y(), width, height) }

        graphics.pose().pushPose()
        graphics.pose().translate(0f, 0f, 2f)
        context.style.frame.getOrNull()?.render(graphics, pos.x(), pos.y(), width, height)
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
        context.removeExpiredParticles()
        return true
    }

    fun setTooltipStack(stack: ItemStack) {
        actualStack = stack
        if (ItemStack.isSameItemSameTags(lastStack, actualStack)) return
        context = TooltipContext(
            stack = actualStack,
            style = TooltipDefinition.aggregateStyleFor(stack),
            label = TooltipLabel.findFor(stack)
        )
    }
}