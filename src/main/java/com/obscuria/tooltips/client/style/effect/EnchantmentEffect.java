package com.obscuria.tooltips.client.style.effect;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import com.obscuria.tooltips.client.style.particle.LineParticle;
import com.obscuria.tooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnchantmentEffect implements TooltipEffect {
    protected final int START_COLOR;
    protected final int END_COLOR;
    protected final int PARTICLE_START_COLOR;
    protected final int PARTICLE_END_COLOR;
    protected final List<TooltipParticle> particles = new ArrayList<>();
    protected float lastParticle = 0f;

    public EnchantmentEffect(int startColor, int endColor, int particleStartColor, int particleEndColor) {
        this.START_COLOR = startColor;
        this.END_COLOR = endColor;
        this.PARTICLE_START_COLOR = particleStartColor;
        this.PARTICLE_END_COLOR = particleEndColor;
    }

    @Override
    public void render(TooltipRenderer renderer, Vec2 pos, Point size) {
        size = new Point(size.x + 8, size.y + 8);
        final Vec2 start = pos.add(-4);
        final float width = 10f + 5f * (float) Math.cos(renderer.time());
        final int startColor = 0x30f00fff;
        final int endColor = 0x00ff00ff;

        Matrix4f matrix4f = renderer.pose().last().pose();
        VertexConsumer buffer = renderer.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x, start.y, 0).color(startColor).endVertex();
        buffer.vertex(matrix4f, start.x, start.y + size.y, 0).color(startColor).endVertex();
        buffer.vertex(matrix4f, start.x + width, start.y + size.y - width, 0).color(endColor).endVertex();
        buffer.vertex(matrix4f, start.x + width, start.y + width, 0).color(endColor).endVertex();

        buffer = renderer.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x, start.y, 0).color(startColor).endVertex();
        buffer.vertex(matrix4f, start.x + width, start.y + width, 0).color(endColor).endVertex();
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + width, 0).color(endColor).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y, 0).color(startColor).endVertex();

        buffer = renderer.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + width, 0).color(endColor).endVertex();
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + size.y - width, 0).color(endColor).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y + size.y, 0).color(startColor).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y, 0).color(startColor).endVertex();

        buffer = renderer.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x + width, start.y + size.y - width, 0).color(endColor).endVertex();
        buffer.vertex(matrix4f, start.x, start.y + size.y, 0).color(startColor).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y + size.y, 0).color(startColor).endVertex();
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + size.y - width, 0).color(endColor).endVertex();

        if (renderer.time() - lastParticle >= 0.1f) {
            lastParticle = renderer.time();
            final Vec2 center = new Vec2(start.x + size.x * 0.5f, start.y + size.y * 0.5f);
            final int edge = RandomUtils.nextInt(1, 5);
            final float mod = RandomUtils.nextFloat(0f, 1f);
            final Vec2 from = switch (edge) {
                case 1 -> new Vec2(start.x, start.y + size.y * mod);
                case 2 -> new Vec2(start.x + size.x, start.y + size.y * mod);
                case 3 -> new Vec2(start.x + size.y * mod, start.y);
                default -> new Vec2(start.x + size.y * mod, start.y + size.y);
            };
            particles.add(new LineParticle(1f, from, renderer.lerp(from, center, 0.25f)));
        }
        renderer.renderParticles(particles);
    }

    @Override
    public void reset() {
        lastParticle = 0f;
        particles.clear();
    }
}
