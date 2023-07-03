package com.obscuria.obscuretooltips.client.style.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;

@SuppressWarnings("unused")
public class CircleParticle extends TooltipParticle {
    protected final int START_COLOR;
    protected final int END_COLOR;
    protected final float RADIUS;
    protected Vec2 end;

    public CircleParticle(float lifetime, Vec2 start, Vec2 end, int startColor, int endColor, float radius) {
        super(lifetime);
        this.START_COLOR = startColor;
        this.END_COLOR = endColor;
        this.RADIUS = radius;
        this.position = start;
        this.end = end;
    }

    @Override
    public void renderParticle(GuiGraphics context, float seconds) {
        final float mod = 1f - (float) Math.pow(1f - seconds / LIFETIME, 3f);
        final float scale = mod < 0.5f ? mod * 2f : mod < 0.8f ? 1f : 1f - (mod - 0.8f) / 0.2f;
        context.pose().pushPose();
        context.pose().translate(
                Mth.lerp(mod, position.x, end.x),
                Mth.lerp(mod, position.y, end.y), 0f);
        context.pose().scale(scale, scale, scale);
        context.pose().pushPose();
        for (float i = 0f; i < 2f; i += 0.2f) {
            final double d1 = Math.PI * (i + 0.1f);
            final double d2 = Math.PI * (i - 0.1f);
            final Vector2f first = new Vector2f((float) (Math.cos(d1) * RADIUS), (float) (Math.sin(d1) * RADIUS));
            final Vector2f second = new Vector2f((float) (Math.cos(d2) * RADIUS), (float) (Math.sin(d2) * RADIUS));
            Matrix4f matrix4f = context.pose().last().pose();
            VertexConsumer vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
            vertexconsumer.vertex(matrix4f, 0, 0, 0).color(START_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, 0, 0, 0).color(START_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, first.x, first.y, 0).color(END_COLOR).endVertex();
            vertexconsumer.vertex(matrix4f, second.x, second.y, 0).color(END_COLOR).endVertex();
        }
        context.pose().popPose();
        context.pose().popPose();
    }
}
