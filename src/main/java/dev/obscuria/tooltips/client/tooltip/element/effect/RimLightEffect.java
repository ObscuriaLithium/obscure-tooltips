package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.element.QuadPalette;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import net.minecraft.util.JsonUtils;

import java.util.List;

@Desugar
public record RimLightEffect(QuadPalette outerPalette, QuadPalette innerPalette) implements TooltipEffect {

    public static RimLightEffect fromJson(JsonObject json) {
        return new RimLightEffect(
                QuadPalette.fromJson(JsonUtils.getJsonObject(json, "outer_palette")),
                QuadPalette.fromJson(JsonUtils.getJsonObject(json, "inner_palette")));
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof RimLightEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {
        final var pX = x - 3f;
        final var pY = y - 3f;
        final var pWidth = width + 6f;
        final var pHeight = height + 6f;

        final var scale = 0.8f + 0.4f * (float) Math.cos(state.timeInSeconds());
        final var offset = Math.min(pWidth, pHeight) * 0.25f * scale;
        
        graphics.pose().pushMatrix();
        GraphicUtils.drawQuad(graphics, pX, pY, pX + offset, pY + offset, pX + pWidth - offset, pY + offset, pX + pWidth, pY,
                outerPalette.topLeft(), innerPalette.topLeft(), innerPalette.topRight(), outerPalette.topRight());
        GraphicUtils.drawQuad(graphics, pX + offset, pY + pHeight - offset, pX, pY + pHeight, pX + pWidth, pY + pHeight, pX + pWidth - offset, pY + pHeight - offset,
                innerPalette.bottomLeft(), outerPalette.bottomLeft(), outerPalette.bottomRight(), innerPalette.bottomRight());
        GraphicUtils.drawQuad(graphics, pX, pY, pX, pY + pHeight, pX + offset, pY + pHeight - offset, pX + offset, pY + offset,
                outerPalette.topLeft(), outerPalette.bottomLeft(), innerPalette.bottomLeft(), innerPalette.topLeft());
        GraphicUtils.drawQuad(graphics, pX + pWidth - offset, pY + offset, pX + pWidth - offset, pY + pHeight - offset, pX + pWidth, pY + pHeight, pX + pWidth, pY,
                innerPalette.topRight(), innerPalette.bottomRight(), outerPalette.bottomRight(), outerPalette.topRight());
        graphics.pose().popMatrix();
    }
}
