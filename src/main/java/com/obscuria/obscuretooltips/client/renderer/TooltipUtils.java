package com.obscuria.obscuretooltips.client.renderer;

import com.mojang.math.Axis;
import com.obscuria.obscuretooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.List;

public final class TooltipUtils {

    @Contract("_ -> new")
    public static Component getRarityName(ItemStack stack) {
        return Component.translatable("rarity." + stack.getRarity().name().toLowerCase() + ".name");
    }

    public static void pushAndMul(GuiGraphics context, Quaternionf quaternionf, Runnable before, Runnable after) {
        context.pose().pushPose();
        before.run();
        context.pose().mulPose(quaternionf);
        after.run();
        context.pose().popPose();
    }

    public static Vec2 lerp(Vec2 from, Vec2 to, float progress) {
        return new Vec2(Mth.lerp(progress, from.x, to.x), Mth.lerp(progress, from.y, to.y));
    }

    public static float angle(Vec2 from, Vec2 to) {
        return (float) Math.atan2(to.y - from.y, to.x - from.x);
    }

    public static void updateAndRenderParticles(List<TooltipParticle> particles, GuiGraphics context) {
        List.copyOf(particles).forEach(particle -> { if (particle.shouldRemove()) particles.remove(particle); });
        particles.forEach(particle -> particle.render(context));
    }

    public static void renderItem(GuiGraphics context, ItemStack stack, Vector3f rot, Vector3f scale) {
        context.pose().pushPose();
        context.pose().translate(0, 0, 500);
        context.pose().scale(scale.x, scale.y, scale.z);
        context.pose().mulPose(Axis.XP.rotationDegrees(rot.x));
        context.pose().mulPose(Axis.YP.rotationDegrees(rot.y));
        context.pose().mulPose(Axis.ZP.rotationDegrees(rot.z));
        context.pose().pushPose();
        context.pose().translate(-8, -8, -150);
        context.renderItem(stack, 0, 0);
        context.pose().popPose();
        context.pose().popPose();
    }
}
