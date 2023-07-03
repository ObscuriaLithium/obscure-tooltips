package com.obscuria.obscuretooltips.client.style.particle;

import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class PurpleStarParticle extends TooltipParticle {
    protected Vec2 end;

    public PurpleStarParticle(float lifetime, Vec2 start, Vec2 end) {
        super(lifetime);
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
        context.pose().mulPose(Axis.ZP.rotation((float) Math.pow(seconds, 4f)));
        context.pose().pushPose();
        context.pose().translate(-0.5f, 0.5f, 0f);
        context.fill(0, 0, 1, 1, 0xffffffff);
        context.fill(0, -1, 1, 0, 0xffff60ff);
        context.fill(0, 1, 1, 2, 0xffff60ff);
        context.fill(-1, 0, 0, 1, 0xffff60ff);
        context.fill(1, 0, 2, 1, 0xffff60ff);
        context.pose().popPose();
        context.pose().popPose();
    }
}
