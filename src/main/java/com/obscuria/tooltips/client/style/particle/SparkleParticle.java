package com.obscuria.tooltips.client.style.particle;

import com.mojang.math.Axis;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

public class SparkleParticle extends TooltipParticle {
    protected final int CENTER_COLOR;
    protected final int EDGE_COLOR;
    protected Vec2 end;

    public SparkleParticle(int centerColor, int edgeColor, float lifetime, Vec2 start, Vec2 end) {
        super(lifetime);
        this.CENTER_COLOR = centerColor;
        this.EDGE_COLOR = edgeColor;
        this.position = start;
        this.end = end;
    }

    @Override
    public void renderParticle(TooltipContext context, float lifetime) {
        final float mod = 1f - (float) Math.pow(1f - lifetime / MAX_LIFETIME, 3f);
        final float scale = mod < 0.5f ? mod * 2f : mod < 0.8f ? 1f : 1f - (mod - 0.8f) / 0.2f;
        context.push(() -> {
            context.translate(
                    Mth.lerp(mod, position.x, end.x),
                    Mth.lerp(mod, position.y, end.y), 0f);
            context.scale(scale, scale, scale);
            context.mul(Axis.ZP.rotation((float) Math.pow(lifetime, 4f)));
            context.push(() -> {
                context.translate(-0.5f, 0.5f, 0f);
                context.fill(0, 0, 1, 1, CENTER_COLOR);
                context.fill(-1, 0, 1, 1, EDGE_COLOR);
                context.fill(1, 0, 1, 1, EDGE_COLOR);
                context.fill(0, -1, 1, 1, EDGE_COLOR);
                context.fill(0, 1, 1, 1, EDGE_COLOR);
            });
        });
    }
}
