package com.obscuria.tooltips.client.style.frame;

import com.mojang.math.Axis;
import com.obscuria.tooltips.ObscureTooltips;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

import java.awt.*;

public class BonesFrame implements TooltipFrame {
    private static final ResourceLocation DECOR = new ResourceLocation(ObscureTooltips.MODID, "textures/tooltips/animated/bones.png");

    @Override
    public void render(TooltipContext context, Vec2 pos, Point size) {
        final int x = (int) pos.x, y = (int) pos.y;
        final int width = size.x, height = size.y;
        final float timeOffset = 2f + (float) Math.cos(context.time()) * 6f;
        final float rotation1 = (float) Math.cos(context.time() * 1.3f + timeOffset) * 0.2f;
        final float rotation2 = (float) Math.cos(context.time() * 1.3f + 0.5f + timeOffset) * 0.2f;
        final float rotation3 = (float) Math.cos(context.time() * 1.3f + 1f + timeOffset) * 0.2f;

        context.push(() -> {
            context.translate(0, 0, 410);

            context.blit(DECOR, x - 5, y - 5, 0, 0, 6, 6, 80, 32);
            context.blit(DECOR, x + width - 1, y - 5, 74, 0, 6, 6, 80, 32);
            context.blit(DECOR, x - 5, y + height - 1, 0, 26, 6, 6, 80, 32);
            context.blit(DECOR, x + width - 1, y + height - 1, 74, 26, 6, 6, 80, 32);

            context.blit(DECOR, x + width / 2 - 10, y - 10, 29, 1, 21, 10, 80, 32);
            context.pushAndMul(Axis.ZP.rotation(rotation3),
                    () -> context.translate(x + width / 2f - 16f, y - 1f, 0f),
                    () -> context.blit(DECOR, -3, -3, 11, 6, 3, 3, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(rotation2),
                    () -> context.translate(x + width / 2f - 11f, y - 1f, 0f),
                    () -> context.blit(DECOR, -6, -5, 14, 4, 6, 5, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(rotation1),
                    () -> context.translate(x + width / 2f - 4f, y - 1f, 0f),
                    () -> context.blit(DECOR, -9, -7, 20, 2, 9, 7, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(-rotation3),
                    () -> context.translate(x + width / 2f + 17f, y - 1f, 0f),
                    () -> context.blit(DECOR, 0, -3, 65, 6, 3, 3, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(-rotation2),
                    () -> context.translate(x + width / 2f + 12f, y - 1f, 0f),
                    () -> context.blit(DECOR, 0, -5, 59, 4, 6, 5, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(-rotation1),
                    () -> context.translate(x + width / 2f + 5f, y - 1f, 0f),
                    () -> context.blit(DECOR, 0, -7, 50, 2, 9, 7, 80, 32));

            context.blit(DECOR, x + width / 2 - 1, y + height + 2, 38, 24, 3, 4, 80, 32);
            context.pushAndMul(Axis.ZP.rotation(-rotation3),
                    () -> context.translate(x + width / 2f - 12f, y + height + 1f, 0f),
                    () -> context.blit(DECOR, -3, 0, 21, 23, 3, 3, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(-rotation2),
                    () -> context.translate(x + width / 2f - 7f, y + height + 1f, 0f),
                    () -> context.blit(DECOR, -6, 0, 24, 23, 6, 5, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(-rotation1),
                    () -> context.translate(x + width / 2f - 1f, y + height + 1f, 0f),
                    () -> context.blit(DECOR, -8, 0, 30, 23, 8, 7, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(rotation3),
                    () -> context.translate(x + width / 2f + 13f, y + height + 1f, 0f),
                    () -> context.blit(DECOR, 0, 0, 55, 23, 3, 3, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(rotation2),
                    () -> context.translate(x + width / 2f + 8f, y + height + 1f, 0f),
                    () -> context.blit(DECOR, 0, 0, 49, 23, 6, 5, 80, 32));
            context.pushAndMul(Axis.ZP.rotation(rotation1),
                    () -> context.translate(x + width / 2f + 2f, y + height + 1f, 0f),
                    () -> context.blit(DECOR, 0, 0, 41, 23, 8, 7, 80, 32));
        });
    }
}
