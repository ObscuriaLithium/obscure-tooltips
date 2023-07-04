package com.obscuria.tooltips.client.style.frame;

import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.awt.*;

public class TextureFrame implements TooltipFrame {
    protected final ResourceLocation TEXTURE;

    public TextureFrame(ResourceLocation texture) {
        this.TEXTURE = texture;
    }

    @Override
    public void render(TooltipRenderer renderer, Vec2 pos, Point size) {
        renderer.blit(TEXTURE, (int) pos.x + size.x / 2 - 30, (int) pos.y - 10, 10, 0, 60, 16, 80, 32);
        renderer.blit(TEXTURE, (int) pos.x + size.x / 2 - 30, (int) pos.y + size.y - 6, 10, 16, 60, 16, 80, 32);
        renderer.blit(TEXTURE, (int) pos.x - 5, (int) pos.y - 5, 0, 0, 10, 10, 80, 32);
        renderer.blit(TEXTURE, (int) pos.x + size.x - 5, (int) pos.y - 5, 70, 0, 10, 10, 80, 32);
        renderer.blit(TEXTURE, (int) pos.x - 5, (int) pos.y + size.y - 5, 0, 22, 10, 10, 80, 32);
        renderer.blit(TEXTURE, (int) pos.x + size.x - 5, (int) pos.y + size.y - 5, 70, 22, 10, 10, 80, 32);
    }
}
