package dev.obscuria.obscure_tooltips.client;

import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.particle.TooltipParticle;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;

@RequiredArgsConstructor
public abstract class ParticleData {
    public final Object source;
    public final float startTime;
    public final Vec2f origin;
    public final Vec2f destination;

    public ParticleStatus status = ParticleStatus.ALIVE;

    public void render(GuiGraphics graphics, TooltipState state, TooltipParticle particle, int x, int y) {
        final var progress = computeProgress(state);
        final var translation = computeTranslation(state, progress);
        final var scale = Math.max(0f,  computeScale(state, progress));
        final var rotation = computeRotation(state, progress);

        graphics.pose().pushMatrix();
        graphics.pose().translate(x + origin.x + translation.x, y + origin.y + translation.y, 0f);
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().rotateRadiansZ(rotation);
        particle.render(graphics, state, this);
        graphics.pose().popMatrix();

        status = (state.timeInSeconds() - startTime) < lifetime()
                ? ParticleStatus.ALIVE
                : ParticleStatus.EXPIRED;
    }

    public float computeProgress(TooltipState state) {
        return MathHelper.clamp(state.timeInSeconds() - startTime, 0f, 1f);
    }

    public Vec2f computeTranslation(TooltipState state, float progress) {
        return new Vec2f(
                (destination.x - origin.x) * progress,
                (destination.y - origin.y) * progress);
    }

    public float computeScale(TooltipState state, float progress) {
        return 1f;
    }

    public float computeRotation(TooltipState state, float progress) {
        return 0f;
    }

    public float lifetime() {
        return 1f;
    }

    public boolean isExpired() {
        return status == ParticleStatus.EXPIRED;
    }
}
