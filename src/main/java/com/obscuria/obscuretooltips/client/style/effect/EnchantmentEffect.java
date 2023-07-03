package com.obscuria.obscuretooltips.client.style.effect;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.obscuria.obscuretooltips.client.renderer.TooltipUtils;
import com.obscuria.obscuretooltips.client.style.particle.PurpleLineParticle;
import com.obscuria.obscuretooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnchantmentEffect implements TooltipEffect {

    private final List<TooltipParticle> particles = new ArrayList<>();
    private float lastParticle = 0f;

    @Override
    public void render(GuiGraphics context, Vec2 pos, Point size, float seconds) {
        size = new Point(size.x + 8, size.y + 8);
        final Vec2 start = pos.add(-4);
        final float width = 10f + 5f * (float) Math.cos(seconds);
        final int startColor = 0x30f00fff;
        final int endColor = 0x00ff00ff;

        Matrix4f matrix4f = context.pose().last().pose();
        VertexConsumer vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        vertexconsumer.vertex(matrix4f, start.x, start.y, 0).color(startColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x, start.y + size.y, 0).color(startColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + width, start.y + size.y - width, 0).color(endColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + width, start.y + width, 0).color(endColor).endVertex();

        vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        vertexconsumer.vertex(matrix4f, start.x, start.y, 0).color(startColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + width, start.y + width, 0).color(endColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + size.x - width, start.y + width, 0).color(endColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + size.x, start.y, 0).color(startColor).endVertex();

        vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        vertexconsumer.vertex(matrix4f, start.x + size.x - width, start.y + width, 0).color(endColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + size.x - width, start.y + size.y - width, 0).color(endColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + size.x, start.y + size.y, 0).color(startColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + size.x, start.y, 0).color(startColor).endVertex();

        vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        vertexconsumer.vertex(matrix4f, start.x + width, start.y + size.y - width, 0).color(endColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x, start.y + size.y, 0).color(startColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + size.x, start.y + size.y, 0).color(startColor).endVertex();
        vertexconsumer.vertex(matrix4f, start.x + size.x - width, start.y + size.y - width, 0).color(endColor).endVertex();

        if (seconds - lastParticle >= 0.1f) {
            lastParticle = seconds;
            final Vec2 center = new Vec2(start.x + size.x * 0.5f, start.y + size.y * 0.5f);
            final int edge = RandomUtils.nextInt(1, 5);
            final float mod = RandomUtils.nextFloat(0f, 1f);
            final Vec2 from = switch (edge) {
                case 1 -> new Vec2(start.x, start.y + size.y * mod);
                case 2 -> new Vec2(start.x + size.x, start.y + size.y * mod);
                case 3 -> new Vec2(start.x + size.y * mod, start.y);
                default -> new Vec2(start.x + size.y * mod, start.y + size.y);
            };
            particles.add(new PurpleLineParticle(1f, from, TooltipUtils.lerp(from, center, 0.25f)));
        }
        TooltipUtils.updateAndRenderParticles(particles, context);
    }

    @Override
    public void reset() {
        lastParticle = 0f;
        particles.clear();
    }
}
