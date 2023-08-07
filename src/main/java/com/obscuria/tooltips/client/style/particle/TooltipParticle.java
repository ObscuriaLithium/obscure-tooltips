package com.obscuria.tooltips.client.style.particle;

import com.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class TooltipParticle {
    protected final long START_TIME;
    protected final float MAX_LIFETIME;
    protected Vec2 position = Vec2.ZERO;

    public TooltipParticle(float lifetime) {
        START_TIME = System.currentTimeMillis();
        MAX_LIFETIME = lifetime;
    }

    public abstract void renderParticle(TooltipContext context, float lifetime);

    public final void render(TooltipContext context) {
        renderParticle(context, (System.currentTimeMillis() - START_TIME) / 1000f);
    }

    public final boolean shouldRemove() {
        return (System.currentTimeMillis() - START_TIME) / 1000f > MAX_LIFETIME;
    }
}
