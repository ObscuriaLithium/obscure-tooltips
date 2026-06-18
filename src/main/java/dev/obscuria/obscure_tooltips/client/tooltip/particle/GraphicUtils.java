package dev.obscuria.obscure_tooltips.client.tooltip.particle;

import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.element.QuadPalette;
import dev.obscuria.obscure_tooltips.util.color.ARGB;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public final class GraphicUtils {
    public static void drawFrame(final GuiGraphics graphics, final int x, final int y, final int width, final int height, final QuadPalette palette) {
        drawFrame(graphics, x, y, width, height, palette.topLeft(), palette.topRight(), palette.bottomLeft(), palette.bottomRight());
    }

    public static void drawFrame(final GuiGraphics graphics, final int x, final int y, final int width, final int height, final ARGB topLeft, final ARGB topRight, final ARGB bottomLeft, final ARGB bottomRight) {
        drawHLine(graphics, x, y, width, topLeft, topRight);
        drawHLine(graphics, x, y + height - 1, width, bottomLeft, bottomRight);
        drawVLine(graphics, x, y + 1, height - 2, topLeft, bottomLeft);
        drawVLine(graphics, x + width - 1, y + 1, height - 2, topRight, bottomRight);
    }

    public static void drawHLine(final GuiGraphics graphics, final int x, final int y, final int length, final ARGB start, final ARGB end) {
        drawRect(graphics, x, y, length, 1, start, end, start, end);
    }

    public static void drawVLine(final GuiGraphics graphics, final int x, final int y, final int length, final ARGB start, final ARGB end) {
        drawRect(graphics, x, y, 1, length, start, start, end, end);
    }

    public static void drawHLineOverlay(final GuiGraphics graphics, final int x, final int y, final int length, final ARGB start, final ARGB end) {
        drawRect(graphics, x, y, length, 1, start, end, start, end);
    }

    public static void drawRect(final GuiGraphics graphics, final int x, final int y, final int width, final int height, final QuadPalette palette) {
        drawRect(graphics, x, y, width, height, palette.topLeft(), palette.topRight(), palette.bottomLeft(), palette.bottomRight());
    }

    public static void drawRect(GuiGraphics graphics, int x, int y, int width, int height, ARGB topLeft, ARGB topRight, ARGB bottomLeft, ARGB bottomRight) {
        final float minX = x;
        final float minY = y;
        final float maxX = x + width;
        final float maxY = y + height;
        drawQuad(minX, minY, topLeft, minX, maxY, bottomLeft, maxX, maxY, bottomRight, maxX, minY, topRight);
    }

    public static void drawQuad(GuiGraphics graphics, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, ARGB topLeft, ARGB topRight, ARGB bottomLeft, ARGB bottomRight) {
        drawQuad(x1, y1, topLeft, x2, y2, topRight, x3, y3, bottomLeft, x4, y4, bottomRight);
    }

    private static void drawQuad(float ax, float ay, ARGB ac, float bx, float by, ARGB bc,
                                 float cx, float cy, ARGB cc, float dx, float dy, ARGB dc) {
        beginState();
        final BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        vertex(buffer, ax, ay, ac);
        vertex(buffer, bx, by, bc);
        vertex(buffer, cx, cy, cc);
        vertex(buffer, dx, dy, dc);
        Tessellator.getInstance().draw();
        endState();
    }

    private static void vertex(BufferBuilder buffer, float x, float y, ARGB color) {
        buffer.pos(x, y, 0.0).color(color.red(), color.green(), color.blue(), color.alpha()).endVertex();
    }

    private static void beginState() {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
    }

    private static void endState() {
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
