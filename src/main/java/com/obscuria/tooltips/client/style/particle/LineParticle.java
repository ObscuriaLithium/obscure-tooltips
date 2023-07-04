package com.obscuria.tooltips.client.style.particle;

import com.mojang.math.Axis;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class LineParticle extends TooltipParticle {
    protected Vec2 end;

    public LineParticle(float lifetime, Vec2 start, Vec2 end) {
        super(lifetime);
        this.position = start;
        this.end = end;
    }

    @Override
    public void renderParticle(TooltipRenderer renderer, float lifetime) {
        final float mod = 1f - (float) Math.pow(1f - lifetime / MAX_LIFETIME, 3f);
        final float scale = mod < 0.5f ? mod*2f : mod < 0.8f ? 1f : 1f - (mod-0.8f)/0.2f;
        final int startColor = 0x80ff80ff;
        final int endColor = 0x00aa40aa;
        renderer.push(() -> {
            renderer.translate(
                    Mth.lerp(mod, position.x, end.x),
                    Mth.lerp(mod, position.y, end.y), 0f);
            renderer.scale(scale, scale, scale);
            renderer.mul(Axis.ZP.rotation(renderer.angle(position, end) + (float) Math.PI * 0.5f));
            renderer.push(() -> {
                renderer.translate(-0.5f, 0.5f, 0f);
                renderer.fillGradient(0, -6, 1, 5, endColor, startColor);
                renderer.fill(0, 0, 1, 1, startColor);
                renderer.fillGradient(0, 1, 1, 5, startColor, endColor);
            });
        });
    }
}
