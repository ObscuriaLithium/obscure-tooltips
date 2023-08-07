package com.obscuria.tooltips.client.style.effect;

import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.style.Effects;
import com.obscuria.tooltips.client.style.particle.EnderParticle;
import com.obscuria.tooltips.client.style.particle.TooltipParticle;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnderEffect implements TooltipEffect {
    protected final int CENTER_COLOR;
    protected final int EDGE_COLOR;
    protected final List<TooltipParticle> particles = new ArrayList<>();
    protected float lastParticle = -0.20f;

    public EnderEffect(int centerColor, int edgeColor) {
        this.CENTER_COLOR = centerColor;
        this.EDGE_COLOR = edgeColor;
    }

    @Override
    public void render(TooltipContext context, Vec2 pos, Point size) {
        if (context.time() - lastParticle >= 0.25f) {
            lastParticle = context.time();
            final Vec2 center = new Vec2(pos.x + 13, pos.y + 13);
            particles.add(new EnderParticle(CENTER_COLOR, EDGE_COLOR, 3f, center, 13f));
        }
        context.renderParticles(particles);
    }

    @Override
    public void reset() {
        lastParticle = -0.20f;
        particles.clear();
    }

    @Override
    public Effects.Order order() {
        return Effects.Order.LAYER_5_FRONT;
    }

    @Override
    public boolean canStackWith(TooltipEffect other) {
        return !(other instanceof EnderEffect);
    }
}
