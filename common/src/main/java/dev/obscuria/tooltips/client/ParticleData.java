package dev.obscuria.tooltips.client;

import com.mojang.math.Axis;
import dev.obscuria.tooltips.client.tooltip.particle.TooltipParticle;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

@RequiredArgsConstructor
public abstract class ParticleData {

    public final Object source;
    public final float startTime;
    public final Vec2 origin;
    public final Vec2 destination;

    public ParticleStatus status = ParticleStatus.ALIVE;

    public void render(GuiGraphics graphics, TooltipState state, TooltipParticle particle, int x, int y) {

        final var progress = computeProgress(state);
        final var translation = computeTranslation(state, progress);
        final var scale = Math.max(0f,  computeScale(state, progress));
        final var rotation = computeRotation(state, progress);

        graphics.pose().pushPose();
        graphics.pose().translate(x + origin.x + translation.x, y + origin.y + translation.y, 0f);
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().mulPose(Axis.ZP.rotation(rotation));
        particle.render(graphics, state, this);
        graphics.pose().popPose();

        status = progress <= 1f ? ParticleStatus.ALIVE : ParticleStatus.EXPIRED;
    }

    public float computeProgress(TooltipState state) {
        return Mth.clamp(state.timeInSeconds() - startTime, 0f, 1f);
    }

    public Vec2 computeTranslation(TooltipState state, float progress) {
        return new Vec2(
                (destination.x - origin.x) * progress,
                (destination.y - origin.y) * progress);
    }

    public float computeScale(TooltipState state, float progress) {
        return 1f;
    }

    public float computeRotation(TooltipState state, float progress) {
        return 0f;
    }

    public boolean isExpired() {
        return status == ParticleStatus.EXPIRED;
    }
}
