package dev.obscuria.tooltips.client.tooltip.element.panel;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.element.QuadPalette;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.tooltips.config.ClientConfig;
import dev.obscuria.tooltips.util.color.ARGB;
import net.minecraft.util.JsonUtils;

@Desugar
public record ColorRectPanel(QuadPalette background, QuadPalette border) implements TooltipPanel {

    public static ColorRectPanel fromJson(JsonObject json) {
        return new ColorRectPanel(
                QuadPalette.fromJson(JsonUtils.getJsonObject(json, "background_palette")),
                QuadPalette.fromJson(JsonUtils.getJsonObject(json, "border_palette")));
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        dropShadow(graphics, x - 3, y - 3, width + 6, height + 6);
        GraphicUtils.drawRect(graphics, x - 3, y - 3, width + 6, height + 6, background);
        GraphicUtils.drawFrame(graphics, x - 3, y - 3, width + 6, height + 6, border);
        GraphicUtils.drawHLine(graphics, x - 3, y - 4, width + 6, background.topLeft(), background.topRight());
        GraphicUtils.drawHLine(graphics, x - 3, y + height + 3, width + 6, background.bottomLeft(), background.bottomRight());
        GraphicUtils.drawVLine(graphics, x - 4, y - 3, height + 6, background.topLeft(), background.bottomLeft());
        GraphicUtils.drawVLine(graphics, x + width + 3, y - 3, height + 6, background.topRight(), background.bottomRight());
    }

    @Override
    public ARGB separatorColor() {
        return border.topLeft().lerp(border.bottomRight(), 0.5f);
    }

    private void dropShadow(GuiGraphics graphics, int x, int y, int width, int height) {
        if (!ClientConfig.SHADOWS_ENABLED.get()) {
            return;
        }

        final int color = (int) (Math.round(ClientConfig.SHADOW_OPACITY.get() * 255.0) << 24);
        graphics.hLine(x + 2, x + width, y + height + 1, color);
        graphics.hLine(x + 3, x + width + 1, y + height + 2, color);
        graphics.vLine(x + width + 1, y + 1, y + height + 1, color);
        graphics.vLine(x + width + 2, y + 2, y + height + 2, color);
        graphics.hLine(x + width, x + width, y + height, color);
        graphics.hLine(x + width + 1, x + width + 1, y + height + 1, color);
    }
}
