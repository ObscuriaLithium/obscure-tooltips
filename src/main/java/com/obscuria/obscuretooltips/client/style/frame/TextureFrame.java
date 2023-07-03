package com.obscuria.obscuretooltips.client.style.frame;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.awt.*;

public class TextureFrame implements TooltipFrame {
    protected final ResourceLocation TEXTURE;

    public TextureFrame(ResourceLocation texture) {
        this.TEXTURE = texture;
    }

    @Override
    public void render(GuiGraphics context, Vec2 pos, Point size, float seconds) {
        context.blit(TEXTURE, (int) pos.x + size.x / 2 - 30, (int) pos.y - 10, 10, 0, 60, 16, 80, 32);
        context.blit(TEXTURE, (int) pos.x + size.x / 2 - 30, (int) pos.y + size.y - 6, 10, 16, 60, 16, 80, 32);
        context.blit(TEXTURE, (int) pos.x - 5, (int) pos.y - 5, 0, 0, 10, 10, 80, 32);
        context.blit(TEXTURE, (int) pos.x + size.x - 5, (int) pos.y - 5, 70, 0, 10, 10, 80, 32);
        context.blit(TEXTURE, (int) pos.x - 5, (int) pos.y + size.y - 5, 0, 22, 10, 10, 80, 32);
        context.blit(TEXTURE, (int) pos.x + size.x - 5, (int) pos.y + size.y - 5, 70, 22, 10, 10, 80, 32);
    }

    public static TextureFrame build(JsonObject params) {
        return new TextureFrame(new ResourceLocation(params.get("texture").getAsString()));
    }
}
