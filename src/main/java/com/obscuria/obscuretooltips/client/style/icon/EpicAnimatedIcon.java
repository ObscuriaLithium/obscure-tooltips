package com.obscuria.obscuretooltips.client.style.icon;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.obscuria.obscuretooltips.client.renderer.TooltipUtils;
import com.obscuria.obscuretooltips.client.style.particle.PurpleStarParticle;
import com.obscuria.obscuretooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EpicAnimatedIcon implements TooltipIcon {

    private final List<TooltipParticle> particles = new ArrayList<>();
    private float lastParticle = 0f;

    @Override
    public void render(GuiGraphics context, ItemStack stack, int x, int y, float seconds) {
        float scale = seconds < 0.25
                ? (1f - (float) Math.pow(1f - seconds * 4, 3)) * 1.5f
                : seconds < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (seconds - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        float scale2 = seconds * 0.5f < 0.25
                ? (1f - (float) Math.pow(1f - seconds * 0.5f * 4, 3)) * 1.5f
                : seconds * 0.5f < 0.5
                ? 1.5f - (1f - (float) Math.pow(1f - (seconds * 0.5f - 0.25f) * 4, 3)) * 0.25f
                : 1.25f;
        context.pose().scale(scale, scale, scale);

        final float centerX = x + 8;
        final float centerY = y + 8;
        final float dist1 = 8f + (float) Math.pow(scale2, 6);
        final float dist2 = (float) Math.pow(scale2, 6) * 1.3f;
        final float mod = seconds < 1f ? (1f - seconds) * 0.03f : 0f;

        for (float i = 0f; i < 2f; i += 0.2f) {
            final double d1 = Math.PI * (i + 0.1f);
            final double d2 = Math.PI * (i - 0.1f);
            final Vector2f first = new Vector2f((float) (Math.cos(d1) * dist2), (float) (Math.sin(d1) * dist2));
            final Vector2f second = new Vector2f((float) (Math.cos(d2) * dist2), (float) (Math.sin(d2) * dist2));
            Matrix4f matrix4f = context.pose().last().pose();
            VertexConsumer vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(0xfff00fff).endVertex();
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(0xfff00fff).endVertex();
            vertexconsumer.vertex(matrix4f, first.x, first.y, 0).color(0x00ff00ff).endVertex();
            vertexconsumer.vertex(matrix4f, second.x, second.y, 0).color(0x00ff00ff).endVertex();
        }

        for (float i = 0f; i < 2f; i += 0.2f) {
            final double d1 = seconds + Math.PI * (i + 0.05f + mod);
            final double d2 = seconds + Math.PI * (i - 0.05f - mod);
            final Vector2f first = new Vector2f((float) (Math.cos(d1) * dist1), (float) (Math.sin(d1) * dist1));
            final Vector2f second = new Vector2f((float) (Math.cos(d2) * dist1), (float) (Math.sin(d2) * dist1));
            Matrix4f matrix4f = context.pose().last().pose();
            VertexConsumer vertexconsumer = context.bufferSource().getBuffer(RenderType.guiOverlay());
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(0xffffffff).endVertex();
            vertexconsumer.vertex(matrix4f, centerX, centerY, 0).color(0xffffffff).endVertex();
            vertexconsumer.vertex(matrix4f, first.x, first.y, 0).color(0x00ff00ff).endVertex();
            vertexconsumer.vertex(matrix4f, second.x, second.y, 0).color(0x00ff00ff).endVertex();
        }

        if (seconds - lastParticle >= 0.1f) {
            lastParticle = seconds;
            final float rotation = (float) (Math.random() * (Math.PI * 2f));
            particles.add(new PurpleStarParticle(1.5f, new Vec2(0, 0), new Vec2(
                    (float) Math.cos(rotation) * 10f,
                    (float) Math.sin(rotation) * 10f)));
        }

        TooltipUtils.updateAndRenderParticles(particles, context);
        TooltipUtils.renderItem(context, stack, new Vector3f(0, 360f
                * (1f - (float) Math.pow(1f - Math.min(1f, seconds * 2f), 3f)),
                0), new Vector3f(1));
    }

    @Override
    public void reset() {
        lastParticle = 0f;
        particles.clear();
    }
}
