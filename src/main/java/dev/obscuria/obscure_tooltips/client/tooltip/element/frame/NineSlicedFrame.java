package dev.obscuria.obscure_tooltips.client.tooltip.element.frame;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

@Desugar
public record NineSlicedFrame(ResourceLocation textureSheet) implements TooltipFrame {

    public static NineSlicedFrame fromJson(JsonObject json) {
        return new NineSlicedFrame(new ResourceLocation(JsonUtils.getString(json, "texture_sheet")));
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.blit(textureSheet, x - 31, y - 31, 1.0f, 1.0f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x + width - 14, y - 31, 93.0f, 1.0f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x - 31, y + height - 14, 1.0f, 93.0f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x + width - 14, y + height - 14, 93.0f, 93.0f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x + width / 2 - 23, y - 31, 47.0f, 1.0f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x + width / 2 - 23, y + height - 14, 47.0f, 93.0f, 45, 45, 140, 140);
    }
}
