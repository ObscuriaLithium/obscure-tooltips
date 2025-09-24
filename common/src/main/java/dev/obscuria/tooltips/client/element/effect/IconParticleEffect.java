package dev.obscuria.tooltips.client.element.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.easing.Easing;
import dev.obscuria.tooltips.client.particle.TooltipParticle;
import dev.obscuria.tooltips.client.renderer.ParticleData;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public record IconParticleEffect(TooltipParticle particle) implements TooltipEffect {

    public static final Codec<IconParticleEffect> CODEC;

    @Override
    public Codec<IconParticleEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof IconParticleEffect);
    }

    @Override
    public void renderIcon(GuiGraphics graphics, TooltipContext context, int x, int y) {

        var lastParticleTime = 0f;
        for (var particle : context.particles()) {
            if (!particle.source.equals(this)) continue;
            particle.render(graphics, context, this.particle, x, y);
            lastParticleTime = Math.max(lastParticleTime, particle.startTime);
        }

        if (context.timeInSeconds() - lastParticleTime < 0.15f) return;
        final var origin = Vec2.ZERO;
        final var direction = RandomUtils.nextFloat(0f, (float) Math.PI * 2f);
        final var destination = new Vec2(12f * (float) Math.sin(direction), 12f * (float) Math.cos(direction));
        context.addParticle(new IconParticle(this, context.timeInSeconds(), origin, destination));
    }

    private static final class IconParticle extends ParticleData {

        public IconParticle(Object source, float startTime, Vec2 origin, Vec2 destination) {
            super(source, startTime, origin, destination);
        }

        public float computeProgress(TooltipContext context) {
            return Easing.EASE_OUT_CUBIC.compute(super.computeProgress(context) * 0.5f);
        }

        public float computeScale(TooltipContext context, float progress) {
            return Easing.EASE_OUT_CUBIC.mergeOut(Easing.EASE_IN_CUBIC, 0.2f).compute(progress * 1.2f);
        }

        public float computeRotation(TooltipContext context, float progress) {
            return progress * 2f;
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                TooltipParticle.CODEC.fieldOf("particle").forGetter(IconParticleEffect::particle)
        ).apply(codec, IconParticleEffect::new));
    }
}
