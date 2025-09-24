package dev.obscuria.tooltips.client.renderer;

import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;
import dev.obscuria.tooltips.client.particle.GraphicUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

public record HeaderComponent(
        int minWidth,
        boolean drawDelimiter,
        TooltipContext context,
        ClientTooltipComponent title,
        ClientTooltipComponent label
) implements ClientTooltipComponent {

    private static final ARGB SEPARATOR_EDGE = Colors.argbOf(0x00ffffff);
    private static final ARGB SEPARATOR_CENTER = Colors.argbOf(0x60ffffff);

    @Override
    public int getHeight() {
        return drawDelimiter ? 25 : 22;
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(minWidth, 22 + Math.max(title.getWidth(font), label.getWidth(font)));
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource source) {
        title.renderText(font, 22 + x, 1 + y, matrix, source);
        label.renderText(font, 22 + x, 1 + y + title.getHeight(), matrix, source);
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        context.style().slot().ifPresent(it -> it.render(graphics, x, y, 20, 20));
        context.style().effects().forEach(it -> it.renderIcon(graphics, context, x + 10, y + 10));
        context.style().icon().ifPresent(it -> it.render(graphics, context, x + 10, y + 10));

        if (!drawDelimiter) return;
        final var length = getWidth(font) / 2;
        GraphicUtils.drawHLine(graphics, x, y + 22, length, SEPARATOR_EDGE, SEPARATOR_CENTER);
        GraphicUtils.drawHLine(graphics, x + length, y + 22, 1 + length, SEPARATOR_CENTER, SEPARATOR_EDGE);
    }
}
