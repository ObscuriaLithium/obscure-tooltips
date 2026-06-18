package dev.obscuria.tooltips.client.tooltip.particle;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.ParticleData;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import dev.obscuria.tooltips.config.ARGBProvider;
import net.minecraft.util.JsonUtils;

@Desugar
public record LineParticle(
        ARGBProvider centerColor,
        ARGBProvider edgeColor,
        Transform transform
) implements TooltipParticle {

    public static LineParticle fromJson(JsonObject json) {
        final ARGBProvider centerColor = ARGBProvider.fromJson(json.get("center_color"));
        final ARGBProvider edgeColor = ARGBProvider.fromJson(json.get("edge_color"));
        final Transform transform = json.has("transform")
                ? Transform.fromJson(JsonUtils.getJsonObject(json, "transform"))
                : Transform.DEFAULT;
        return new LineParticle(centerColor, edgeColor, transform);
    }

    @Override
    public void render(GuiGraphics graphics, TooltipState state, ParticleData data) {
        graphics.pose().pushMatrix();
        transform.apply(graphics);
        graphics.pose().translate(0f, -0.5f, 0f);
        GraphicUtils.drawHLineOverlay(graphics, -8, 0, 8, edgeColor.get(), centerColor.get());
        GraphicUtils.drawHLineOverlay(graphics, 0, 0, 8, centerColor.get(), edgeColor.get());
        graphics.pose().popMatrix();
    }
}
