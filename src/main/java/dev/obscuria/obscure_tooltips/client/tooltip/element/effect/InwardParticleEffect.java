package dev.obscuria.obscure_tooltips.client.tooltip.element.effect;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.ParticleData;
import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.particle.TooltipParticle;
import dev.obscuria.obscure_tooltips.util.easing.Easing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.Vec2f;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

@Desugar
public record InwardParticleEffect(TooltipParticle particle) implements TooltipEffect {

    public static InwardParticleEffect fromJson(JsonObject json) {
        return new InwardParticleEffect(TooltipParticle.fromJson(JsonUtils.getJsonObject(json, "particle")));
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof InwardParticleEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {
        var lastParticleTime = 0f;
        for (var particle : state.particles) {
            if (!particle.source.equals(this)) continue;
            particle.render(graphics, state, this.particle, x, y);
            lastParticleTime = Math.max(lastParticleTime, particle.startTime);
        }

        if (state.timeInSeconds() - lastParticleTime < 0.2f) return;

        final var edge = RandomUtils.nextInt(1, 5);
        final var ratio = RandomUtils.nextFloat(0f, 1f);
        final var center = new Vec2f(width * 0.5f, height * 0.5f);
        final var origin = switch (edge) {
            case 1 -> new Vec2f(width * ratio, 0f);
            case 2 -> new Vec2f(width * ratio, (float) height);
            case 3 -> new Vec2f(0f, height * ratio);
            case 4 -> new Vec2f((float) width, height * ratio);
            default -> Vec2f.ZERO;
        };

        state.addParticle(new InwardParticle(this, state.timeInSeconds(), origin, center));
    }

    private static final class InwardParticle extends ParticleData {

        public InwardParticle(Object source, float startTime, Vec2f origin, Vec2f destination) {
            super(source, startTime, origin, destination);
        }

        @Override
        public float computeProgress(TooltipState state) {
            return Easing.EASE_OUT_CUBIC.compute(super.computeProgress(state));
        }

        @Override
        public Vec2f computeTranslation(TooltipState state, float progress) {
            final var base = super.computeTranslation(state, progress);
            return new Vec2f(base.x * 0.4F, base.y * 0.4F);
        }

        @Override
        public float computeScale(TooltipState state, float progress) {
            return Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_CUBIC, 0.2f).compute(progress * 1.25f);
        }

        @Override
        public float computeRotation(TooltipState state, float progress) {
            return (float) Math.atan2(destination.y - origin.y, destination.x - origin.x);
        }
    }
}
