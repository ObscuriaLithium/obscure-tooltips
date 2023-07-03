package com.obscuria.obscuretooltips.client.style.effect;

import com.obscuria.obscuretooltips.client.renderer.TooltipUtils;
import com.obscuria.obscuretooltips.client.style.particle.PurpleStarParticle;
import com.obscuria.obscuretooltips.client.style.particle.TooltipParticle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TailsEffect implements TooltipEffect {
    private final List<TooltipParticle> particles = new ArrayList<>();
    private float lastParticle = 0f;

    @Override
    public void render(GuiGraphics context, Vec2 pos, Point size, float seconds) {
        seconds *= 0.5f;
        size = new Point(size.x + 6, size.y + 6);
        final Vec2 start = pos.add(-3);
        final Vec2 tail1 = calculateTail(start, size, seconds, 0f);
        final Vec2 tail2 = calculateTail(start, size, seconds, 0.5f);

        if (seconds - lastParticle >= 0.02f) {
            lastParticle = seconds;
            final float rotation = (float) (Math.random() * (Math.PI * 2f));
            final float radius = 2f;
            particles.add(new PurpleStarParticle(1f, tail1, new Vec2(
                    tail1.x + (float) Math.cos(rotation) * radius,
                    tail1.y + (float) Math.sin(rotation) * radius)));
            particles.add(new PurpleStarParticle(1f, tail2, new Vec2(
                    tail2.x + (float) Math.cos(rotation) * radius,
                    tail2.y + (float) Math.sin(rotation) * radius)));
        }
        TooltipUtils.updateAndRenderParticles(particles, context);
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
}
