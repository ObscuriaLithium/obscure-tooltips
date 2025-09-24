package dev.obscuria.tooltips.client.renderer;

import com.mojang.math.Axis;
import dev.obscuria.tooltips.client.particle.TooltipParticle;
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

    public void render(GuiGraphics graphics, TooltipContext context, TooltipParticle particle, int x, int y) {

        final var progress = computeProgress(context);
        final var translation = computeTranslation(context, progress);
        final var scale = computeScale(context, progress);
        final var rotation = computeRotation(context, progress);

        graphics.pose().pushPose();
        graphics.pose().translate(x + origin.x + translation.x, y + origin.y + translation.y, 0f);
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().mulPose(Axis.ZP.rotation(rotation));
        particle.render(graphics, context, this);
        graphics.pose().popPose();

        status = progress <= 1f ? ParticleStatus.ALIVE : ParticleStatus.EXPIRED;
    }

    public float computeProgress(TooltipContext context) {
        return Mth.clamp(context.timeInSeconds() - startTime, 0f, 1f);
    }

    public Vec2 computeTranslation(TooltipContext context, float progress) {
        return new Vec2(
                (destination.x - origin.x) * progress,
                (destination.y - origin.y) * progress);
    }

    public float computeScale(TooltipContext context, float progress) {
        return 1f;
    }

    public float computeRotation(TooltipContext context, float progress) {
        return 0f;
    }
}
