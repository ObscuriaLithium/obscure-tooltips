package dev.obscuria.tooltips.client.tooltip.element.slot;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.element.QuadPalette;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.tooltips.config.ARGBProvider;
import dev.obscuria.tooltips.util.color.ARGB;
import net.minecraft.util.JsonUtils;

@Desugar
public record ColorRectSlot(QuadPalette palette, ARGB borders) implements TooltipSlot {

    public static ColorRectSlot fromJson(JsonObject json) {
        return new ColorRectSlot(
                QuadPalette.fromJson(JsonUtils.getJsonObject(json, "palette")),
                ARGBProvider.parseLiteral(json.get("borders")));
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        GraphicUtils.drawRect(graphics, x + 1, y + 1, width - 2, height - 2, palette);
        GraphicUtils.drawHLine(graphics, x + 1, y, width - 2, borders, borders);
        GraphicUtils.drawHLine(graphics, x + 1, y + height - 1, width - 2, borders, borders);
        GraphicUtils.drawVLine(graphics, x, y + 1, height - 2, borders, borders);
        GraphicUtils.drawVLine(graphics, x + width - 1, y + 1, height - 2, borders, borders);
    }
}
