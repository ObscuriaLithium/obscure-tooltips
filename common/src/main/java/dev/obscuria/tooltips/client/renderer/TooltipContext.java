package dev.obscuria.tooltips.client.renderer;

import dev.obscuria.tooltips.client.TooltipLabel;
import dev.obscuria.tooltips.client.TooltipStyle;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public record TooltipContext(
        ItemStack stack,
        TooltipStyle style,
        @Nullable TooltipLabel label,
        Long startTime,
        List<ParticleData> particles) {

    public TooltipContext(ItemStack stack, TooltipStyle style, @Nullable TooltipLabel label) {
        this(stack, style, label, Util.getMillis(), Lists.newArrayList());
    }

    public TooltipContext() {
        this(ItemStack.EMPTY, TooltipStyle.EMPTY, null, Util.getMillis(), Lists.newArrayList());
    }

    public float timeInSeconds() {
        return (Util.getMillis() - startTime) * 0.001f;
    }

    public void addParticle(ParticleData particle) {
        particles.add(particle);
    }

    public void forEachParticle(Object source, Consumer<ParticleData> consumer) {
        for (var particle : particles) {
            if (!particle.source.equals(source)) continue;
            consumer.accept(particle);
        }
    }

    public void removeExpiredParticles() {
        particles.removeIf(it -> it.status == ParticleStatus.EXPIRED);
    }
}
