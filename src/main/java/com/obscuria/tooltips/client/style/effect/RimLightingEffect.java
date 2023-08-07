package com.obscuria.tooltips.client.style.effect;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.style.particle.LineParticle;
import com.obscuria.tooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.RandomUtils;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RimLightingEffect implements TooltipEffect {
    protected final int START;
    protected final int END;
    protected final int PARTICLE_CENTER;
    protected final int PARTICLE_EDGE;
    protected final List<TooltipParticle> particles = new ArrayList<>();
    protected float lastParticle = 0f;

    public RimLightingEffect(int start, int end, int particleCenter, int particleEdge) {
        this.START = start;
        this.END = end;
        this.PARTICLE_CENTER = particleCenter;
        this.PARTICLE_EDGE = particleEdge;
    }

    @Override
    public void render(TooltipContext context, Vec2 pos, Point size) {
        size = new Point(size.x + 8, size.y + 8);
        final Vec2 start = pos.add(-4);
        final float width = 10f + 5f * (float) Math.cos(context.time());

        Matrix4f matrix4f = context.pose().last().pose();
        VertexConsumer buffer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x, start.y, 0).color(START).endVertex();
        buffer.vertex(matrix4f, start.x, start.y + size.y, 0).color(START).endVertex();
        buffer.vertex(matrix4f, start.x + width, start.y + size.y - width, 0).color(END).endVertex();
        buffer.vertex(matrix4f, start.x + width, start.y + width, 0).color(END).endVertex();

        buffer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x, start.y, 0).color(START).endVertex();
        buffer.vertex(matrix4f, start.x + width, start.y + width, 0).color(END).endVertex();
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + width, 0).color(END).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y, 0).color(START).endVertex();

        buffer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + width, 0).color(END).endVertex();
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + size.y - width, 0).color(END).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y + size.y, 0).color(START).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y, 0).color(START).endVertex();

        buffer = context.bufferSource().getBuffer(RenderType.guiOverlay());
        buffer.vertex(matrix4f, start.x + width, start.y + size.y - width, 0).color(END).endVertex();
        buffer.vertex(matrix4f, start.x, start.y + size.y, 0).color(START).endVertex();
        buffer.vertex(matrix4f, start.x + size.x, start.y + size.y, 0).color(START).endVertex();
        buffer.vertex(matrix4f, start.x + size.x - width, start.y + size.y - width, 0).color(END).endVertex();

        if (context.time() - lastParticle >= 0.1f) {
            lastParticle = context.time();
            final Vec2 center = new Vec2(start.x + size.x * 0.5f, start.y + size.y * 0.5f);
            final int edge = RandomUtils.nextInt(1, 5);
            final float mod = RandomUtils.nextFloat(0f, 1f);
            final Vec2 from = switch (edge) {
                case 1 -> new Vec2(start.x, start.y + size.y * mod);
                case 2 -> new Vec2(start.x + size.x, start.y + size.y * mod);
                case 3 -> new Vec2(start.x + size.y * mod, start.y);
                default -> new Vec2(start.x + size.y * mod, start.y + size.y);
            };
            particles.add(new LineParticle(PARTICLE_CENTER, PARTICLE_EDGE, 1f, from, context.lerp(from, center, 0.25f)));
        }
        context.renderParticles(particles);
    }

    @Override
    public void reset() {
        lastParticle = 0f;
        particles.clear();
    }

    @Override
    public boolean canStackWith(TooltipEffect other) {
        return !(other instanceof RimLightingEffect);
    }
}
