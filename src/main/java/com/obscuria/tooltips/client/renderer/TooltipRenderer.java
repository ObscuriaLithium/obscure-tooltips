package com.obscuria.tooltips.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.obscuria.tooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class TooltipRenderer {
    private final GuiGraphics CONTEXT;
    private ItemStack stack = ItemStack.EMPTY;
    private float seconds = 0f;

    public TooltipRenderer(GuiGraphics context) {
        this.CONTEXT = context;
    }

    public void define(ItemStack stack, float seconds) {
        this.stack = stack;
        this.seconds = seconds;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public float time() {
        return this.seconds;
    }

    public GuiGraphics context() {
        return this.CONTEXT;
    }

    public PoseStack pose() {
        return context().pose();
    }

    public MultiBufferSource.BufferSource bufferSource() {
        return context().bufferSource();
    }

    @SuppressWarnings("deprecation")
    public void drawManaged(Runnable runnable) {
        context().drawManaged(runnable);
    }

    public void renderItem(Vector3f rot, Vector3f scale) {
        push(() -> {
            translate(0, 0, 500);
            scale(scale.x, scale.y, scale.z);
            mul(Axis.XP.rotationDegrees(rot.x));
            mul(Axis.YP.rotationDegrees(rot.y));
            mul(Axis.ZP.rotationDegrees(rot.z));
            push(() -> {
                translate(-8, -8, -150);
                context().renderItem(stack, 0, 0);
            });
        });
    }

    public void renderParticles(List<TooltipParticle> particles) {
        List.copyOf(particles).forEach(particle -> { if (particle.shouldRemove()) particles.remove(particle); });
        particles.forEach(particle -> particle.render(this));
    }

    public void fill(int x, int y, int width, int height, int color) {
        context().fill(x, y, x + width, y + height, color);
    }

    public void fillGradient(int x, int y, int width, int height, int start, int end) {
        context().fillGradient(x, y, x + width, y + height, start, end);
    }

    public void blit(ResourceLocation texture, int x, int y, int xTex, int yTex, int width, int height, int widthTex, int heightTex) {
        context().blit(texture, x, y, xTex, yTex, width, height, widthTex, heightTex);
    }

    public void push(Runnable runnable) {
        context().pose().pushPose();
        try { runnable.run(); } catch (Exception ignored) {}
        context().pose().popPose();
    }

    public void pushAndMul(Quaternionf quaternionf, Runnable before, Runnable after) {
        context().pose().pushPose();
        try { before.run(); } catch (Exception ignored) {}
        context().pose().mulPose(quaternionf);
        try { after.run(); } catch (Exception ignored) {}
        context().pose().popPose();
    }

    public Vec2 lerp(Vec2 from, Vec2 to, float progress) {
        return new Vec2(Mth.lerp(progress, from.x, to.x), Mth.lerp(progress, from.y, to.y));
    }

    public float angle(Vec2 from, Vec2 to) {
        return (float) Math.atan2(to.y - from.y, to.x - from.x);
    }

    public void flush() {
        context().flush();
    }
    public int width() {
        return context().guiWidth();
    }
    public int height() {
        return context().guiHeight();
    }
    public void translate(float x, float y, float z) {
        context().pose().translate(x, y, z);
    }
    public void scale(float x, float y, float z) {
        context().pose().scale(x, y, z);
    }
    public void mul(Quaternionf quat) {
        context().pose().mulPose(quat);
    }
}
