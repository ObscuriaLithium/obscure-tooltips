package dev.obscuria.tooltips.client.particle;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.renderer.ParticleData;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TooltipParticle {

    Codec<TooltipParticle> CODEC = TooltipsRegistries.TOOLTIP_PARTICLE_TYPE.byNameCodec().dispatch(TooltipParticle::codec, Function.identity());

    Codec<? extends TooltipParticle> codec();

    void render(GuiGraphics graphics, TooltipContext context, ParticleData data);

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends TooltipParticle>>> registrar) {

        registrar.accept("texture", () -> TextureParticle.CODEC);
        registrar.accept("line", () -> LineParticle.CODEC);
    }
}
