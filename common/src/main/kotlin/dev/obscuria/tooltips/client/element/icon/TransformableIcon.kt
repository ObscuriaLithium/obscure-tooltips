package dev.obscuria.tooltips.client.element.icon

import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.extension.GraphicsExtensions.scale
import dev.obscuria.tooltips.extension.GraphicsExtensions.translate
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.jvm.optionals.getOrDefault

abstract class TransformableIcon(
    val offset: Optional<Vec3>,
    val scale: Optional<Float>
) : ITooltipIcon {

    override fun render(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {

        graphics.pose().pushPose()
        graphics.pose().translate(x.toFloat(), y.toFloat(), 150f)
        graphics.pose().translate(this.offset.getOrDefault(Vec3.ZERO))
        graphics.pose().scale(this.scale.getOrDefault(1f))
        applyScale(graphics, context, x, y)
        applyRotation(graphics, context, x, y)

        graphics.pose().pushPose()
        graphics.pose().translate(-8f, -8f, -150f)
        renderIcon(graphics, context, x, y)
        graphics.pose().popPose()

        graphics.pose().popPose()
    }

    open fun renderIcon(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {
        graphics.renderItem(context.stack, 0, 0)
    }

    open fun applyScale(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {}

    open fun applyRotation(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {}
}