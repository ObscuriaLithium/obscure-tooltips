package com.obscuria.tooltips.client.style.effect;

import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.style.particle.SparkleParticle;
import com.obscuria.tooltips.client.style.particle.TooltipParticle;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TailsEffect implements TooltipEffect {
    private final List<TooltipParticle> particles = new ArrayList<>();
    private float lastParticle = 0f;

    @Override
    public void render(TooltipContext context, Vec2 pos, Point size) {
        final float time = context.time() * 0.5f;
        size = new Point(size.x + 6, size.y + 6);
        final Vec2 start = pos.add(-3);
        final Vec2 tail1 = calculateTail(start, size, time, 0f);
        final Vec2 tail2 = calculateTail(start, size, time, 0.5f);

        if (time - lastParticle >= 0.02f) {
            lastParticle = time;
            final float rotation = (float) (Math.random() * (Math.PI * 2f));
            final float radius = 2f;
            particles.add(new SparkleParticle(0xffffffff, 0xffff60ff,
                    1f, tail1, new Vec2(
                    tail1.x + (float) Math.cos(rotation) * radius,
                    tail1.y + (float) Math.sin(rotation) * radius)));
            particles.add(new SparkleParticle(0xffffffff, 0xffff60ff,
                    1f, tail2, new Vec2(
                    tail2.x + (float) Math.cos(rotation) * radius,
                    tail2.y + (float) Math.sin(rotation) * radius)));
        }
        context.renderParticles(particles);
    }

    @Override
    public void reset() {
        lastParticle = 0f;
        particles.clear();
    }

    private Vec2 calculateTail(Vec2 pos, Point size, float seconds, float offset) {
        final float verticalMod = size.y / 1f / size.x;
        final float timelapse = (seconds + offset*(2f+verticalMod*2f)) % (2f+verticalMod*2f);
        return timelapse < 1f ? new Vec2(pos.x + size.x*timelapse, pos.y)
                        : timelapse < 1f + verticalMod ? new Vec2(pos.x+size.x, pos.y + size.y*((timelapse-1f)/verticalMod))
                        : timelapse < 2f + verticalMod ? new Vec2((pos.x+size.x) - size.x*(timelapse-(1f+verticalMod)), pos.y+size.y)
                        : new Vec2(pos.x, (pos.y+size.y) - size.y*((timelapse-(2f+verticalMod))/verticalMod));
    }

    @Override
    public boolean canStackWith(TooltipEffect other) {
        return !(other instanceof TailsEffect);
    }
}
