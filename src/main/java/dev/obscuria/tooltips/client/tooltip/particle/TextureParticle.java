package dev.obscuria.tooltips.client.tooltip.particle;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.ParticleData;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

@Desugar
public record TextureParticle(
        ResourceLocation texture,
        Transform transform
) implements TooltipParticle {

    public static TextureParticle fromJson(JsonObject json) {
        final ResourceLocation texture = new ResourceLocation(JsonUtils.getString(json, "texture"));
        final Transform transform = json.has("transform")
                ? Transform.fromJson(JsonUtils.getJsonObject(json, "transform"))
                : Transform.DEFAULT;
        return new TextureParticle(texture, transform);
    }

    @Override
    public void render(GuiGraphics graphics, TooltipState state, ParticleData data) {
        graphics.pose().pushMatrix();
        transform.apply(graphics);
        graphics.blit(texture, -8, -8, 0f, 0f, 16, 16, 16, 16);
        graphics.pose().popMatrix();
    }
}
