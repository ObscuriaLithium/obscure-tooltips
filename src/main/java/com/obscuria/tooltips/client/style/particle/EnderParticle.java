package com.obscuria.tooltips.client.style.particle;

import com.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.RandomUtils;

public class EnderParticle extends TooltipParticle {
    protected final int CENTER_COLOR;
    protected final int EDGE_COLOR;
    protected Vec2 start;

    public EnderParticle(int centerColor, int edgeColor, float lifetime, Vec2 center, float range) {
        super(lifetime);
        this.CENTER_COLOR = centerColor;
        this.EDGE_COLOR = edgeColor;
        this.position = center;
        final float rot = RandomUtils.nextFloat(0, 360);
        this.start = center.add(new Vec2((float)Math.cos(rot)*range, (float)Math.sin(rot)*range));
    }

    @Override
    public void renderParticle(TooltipContext context, float lifetime) {
        final float mod = 1f - (float) Math.pow(1f - lifetime / MAX_LIFETIME, 3f);
        final float scale = (mod < 0.4f ? (float)Math.pow(mod/0.4f, 3f)
                : mod < 0.9f ? 1f - (float)Math.pow((mod-0.4f)/0.5f, 3f)
                : 0f) * 1.2f;
        context.push(() -> {
            context.translate(
                    Mth.lerp((float) Math.pow(mod, 4), start.x, position.x),
                    Mth.lerp((float) Math.pow(mod, 4), start.y, position.y), 0f);
            context.scale(scale, scale, scale);
            context.push(() -> {
                context.translate(-0.5f, 0.5f, 0f);
                context.fill(0, 0, 1, 1, CENTER_COLOR);
                context.fill(1, 1, 1, 1, EDGE_COLOR);
                context.fill(-1, -1, 1, 1, EDGE_COLOR);
                context.fill(-1, 1, 1, 1, EDGE_COLOR);
                context.fill(1, -1, 1, 1, EDGE_COLOR);
            });
        });
    }
}
