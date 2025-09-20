package dev.obscuria.tooltips.extension

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.obscuria.fragmentum.tools.ARGB
import dev.obscuria.fragmentum.tools.argbOf
import dev.obscuria.tooltips.client.element.QuadPalette
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f

object GraphicsExtensions {

    private val WHITE = argbOf(0xFFFFFFFF)

    fun setShaderColor(color: ARGB) {
        RenderSystem.setShaderColor(color.component2(), color.component3(), color.component4(), color.component1())
    }

    fun resetShaderColor() {
        setShaderColor(WHITE)
    }

    fun PoseStack.translate(vec: Vec3) {
        this.translate(vec.x(), vec.y(), vec.z())
    }

    fun PoseStack.scale(value: Float) {
        this.scale(value, value, value)
    }

    fun GuiGraphics.drawFrame(x: Int, y: Int, width: Int, height: Int, palette: QuadPalette) {
        this.drawFrame(x, y, width, height, palette.topLeft, palette.topRight, palette.bottomLeft, palette.bottomRight)
    }

    fun GuiGraphics.drawFrame(
        x: Int, y: Int, width: Int, height: Int,
        topLeft: ARGB, topRight: ARGB, bottomLeft: ARGB, bottomRight: ARGB
    ) {
        this.drawHLine(x, y, width, topLeft, topRight)
        this.drawHLine(x, y + height - 1, width, bottomLeft, bottomRight)
        this.drawVLine(x, y + 1, height - 2, topLeft, bottomLeft)
        this.drawVLine(x + width - 1, y + 1, height - 2, topRight, bottomRight)
    }

    fun GuiGraphics.drawHLine(x: Int, y: Int, length: Int, start: ARGB, end: ARGB) {
        this.drawRect(RenderType.gui(), x, y, length, 1, start, end, start, end)
    }

    fun GuiGraphics.drawVLine(x: Int, y: Int, length: Int, start: ARGB, end: ARGB) {
        this.drawRect(RenderType.gui(), x, y, 1, length, start, start, end, end)
    }

    fun GuiGraphics.drawHLineOverlay(x: Int, y: Int, length: Int, start: ARGB, end: ARGB) {
        this.drawRect(RenderType.guiOverlay(), x, y, length, 1, start, end, start, end)
    }

    fun GuiGraphics.drawVLineOverlay(x: Int, y: Int, length: Int, start: ARGB, end: ARGB) {
        this.drawRect(RenderType.guiOverlay(), x, y, 1, length, start, start, end, end)
    }

    fun GuiGraphics.drawRect(x: Int, y: Int, width: Int, height: Int, palette: QuadPalette) {
        drawRect(RenderType.gui(), x, y, width, height, palette.topLeft, palette.topRight, palette.bottomLeft, palette.bottomRight)
    }

    fun GuiGraphics.drawRect(
        type: RenderType, x: Int, y: Int, width: Int, height: Int,
        topLeft: ARGB, topRight: ARGB, bottomLeft: ARGB, bottomRight: ARGB
    ) {
        val consumer = this.bufferSource().getBuffer(type)
        val matrix4f = this.pose().last().pose()
        val minX = x.toFloat()
        val minY = y.toFloat()
        val maxX = (x + width).toFloat()
        val maxY = (y + height).toFloat()
        consumer.vertex(matrix4f, minX, minY, 0F).color(topLeft).endVertex()
        consumer.vertex(matrix4f, minX, maxY, 0F).color(bottomLeft).endVertex()
        consumer.vertex(matrix4f, maxX, maxY, 0F).color(bottomRight).endVertex()
        consumer.vertex(matrix4f, maxX, minY, 0F).color(topRight).endVertex()
    }

    fun VertexConsumer.vertex(matrix: Matrix4f, x: Int, y: Int, z: Int = 0): VertexConsumer {
        return this.vertex(matrix, x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun VertexConsumer.vertex(matrix: Matrix4f, x: Double, y: Double, z: Double = 0.0): VertexConsumer {
        return this.vertex(matrix, x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun VertexConsumer.color(color: ARGB): VertexConsumer {
        return this.color(color.component2(), color.component3(), color.component4(), color.component1())
    }
}