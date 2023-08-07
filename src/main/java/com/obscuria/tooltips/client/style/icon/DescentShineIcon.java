package com.obscuria.tooltips.client.style.icon;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.style.particle.SparkleParticle;
import com.obscuria.tooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class DescentShineIcon implements TooltipIcon {

    protected final int CENTER_COLOR;
    protected final int START_COLOR;
    protected final int END_COLOR;
    protected final int PARTICLE_CENTER_COLOR;
    protected final int PARTICLE_EDGE_COLOR;
    protected final List<TooltipParticle> particles = new ArrayList<>();
    protected float lastParticle = 0f;

    public DescentShineIcon(int centerColor, int startColor, int endColor, int particleCenterColor, int particleEdgeColor) {
        this.CENTER_COLOR = centerColor;
        this.START_COLOR = startColor;
        this.END_COLOR = endColor;
        this.PARTICLE_CENTER_COLOR = particleCenterColor;
        this.PARTICLE_EDGE_COLOR = particleEdgeColor;
    }

    @Override
    public void render(TooltipContext context, int x, int y) {
        float scale = context.time() < 0.25
                ? (1f - (float) Math.pow(1f - context.time() * 4, 3)) * 1.5f
                : context.time() < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (context.time() - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        float scale2 = context.time() * 0.5f < 0.25
                ? (1f - (float) Math.pow(1f - context.time() * 0.5f * 4, 3)) * 1.5f
                : context.time() * 0.5f < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (context.time() * 0.5f - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        context.pose().scale(scale, scale, scale);

        final float centerX = x + 8;
        final float centerY = y + 8;
        final float dist1 = 8f + (float) Math.pow(scale2, 6);
        final float dist2 = (float) Math.pow(scale2, 6) * 1.3f;
        final float mod = context.time() < 1f ? (1f - context.time()) * 0.03f : 0f;

        for (float i = 0f; i < 2f; i += 0.2f) {
            final double d1 = Math.PI * (i + 0.1f);
            final double d2 = Math.PI * (i - 0.1f);
            final Vector2f first = new Vector2f((float) (Math.cos(d1) * dist2), (float) (Math.sin(d1) * dist2));
            final Vector2f second = new Vector2f((float) (Math.cos(d2) * dist2), (float) (Math.sin(d2) * dist2));
            Matrix4f matrix4f = context.pose().last().pose();
            VertexConsumer vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(START_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(START_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, first.x, first.y, 0).color(END_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, second.x, second.y, 0).color(END_COLOR).endVertex();
        }

        for (float i = 0f; i < 2f; i += 0.2f) {
            final double d1 = context.time() + Math.PI * (i + 0.05f + mod);
            final double d2 = context.time() + Math.PI * (i - 0.05f - mod);
            final Vector2f first = new Vector2f((float) (Math.cos(d1) * dist1), (float) (Math.sin(d1) * dist1));
            final Vector2f second = new Vector2f((float) (Math.cos(d2) * dist1), (float) (Math.sin(d2) * dist1));
            Matrix4f matrix4f = context.pose().last().pose();
            VertexConsumer vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(CENTER_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(CENTER_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, first.x, first.y, 0).color(END_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, second.x, second.y, 0).color(END_COLOR).endVertex();
        }

        if (context.time() - lastParticle >= 0.1f) {
            lastParticle = context.time();
            final float rotation = (float) (Math.random() * (Math.PI * 2f));
            particles.add(new SparkleParticle(PARTICLE_CENTER_COLOR, PARTICLE_EDGE_COLOR,
                    1.5f, new Vec2(0, 0), new Vec2(
                    (float) Math.cos(rotation) * 10f,
                    (float) Math.sin(rotation) * 10f)));
        }

        context.renderParticles(particles);
        context.renderItem(
                new Vector3f(0, 360f * (1f - (float) Math.pow(1f - Math.min(1f, context.time() * 2f), 3f)), 0),
                new Vector3f(1));
    }

    @Override
    public void reset() {
        lastParticle = 0f;
        particles.clear();
    }
}
