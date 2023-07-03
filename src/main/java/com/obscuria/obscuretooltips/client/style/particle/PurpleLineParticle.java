package com.obscuria.obscuretooltips.client.style.particle;

import com.mojang.math.Axis;
import com.obscuria.obscuretooltips.client.renderer.TooltipUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class PurpleLineParticle extends TooltipParticle {
    protected Vec2 end;

    public PurpleLineParticle(float lifetime, Vec2 start, Vec2 end) {
        super(lifetime);
        this.position = start;
        this.end = end;
    }

    @Override
    public void renderParticle(GuiGraphics context, float seconds) {
        final float mod = 1f - (float) Math.pow(1f - seconds / LIFETIME, 3f);
        final float scale = mod < 0.5f ? mod*2f : mod < 0.8f ? 1f : 1f - (mod-0.8f)/0.2f;
        final int startColor = 0x80ff80ff;
        final int endColor = 0x00aa40aa;
        context.pose().pushPose();
        context.pose().translate(
                Mth.lerp(mod, position.x, end.x),
                Mth.lerp(mod, position.y, end.y), 0f);
        context.pose().scale(scale, scale, scale);
        context.pose().mulPose(Axis.ZP.rotation(TooltipUtils.angle(position, end) + (float)Math.PI*0.5f));
        context.pose().pushPose();
        context.pose().translate(-0.5f, 0.5f, 0f);
        context.fillGradient(0, -5, 1, 0, endColor, startColor);
        context.fill(0, 0, 1, 1, startColor);
        context.fillGradient(0, 1, 1, 6, startColor, endColor);
        context.pose().popPose();
        context.pose().popPose();
    }
}
