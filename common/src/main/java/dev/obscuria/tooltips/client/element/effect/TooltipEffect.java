package dev.obscuria.tooltips.client.element.effect;

import com.mojang.serialization.Codec;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import dev.obscuria.tooltips.registry.TooltipsRegistries;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TooltipEffect {

    Codec<TooltipEffect> DIRECT_CODEC = TooltipsRegistries.TOOLTIP_EFFECT_TYPE.byNameCodec().dispatch(TooltipEffect::codec, Function.identity());
    Codec<TooltipEffect> CODEC = TooltipsRegistries.Resource.TOOLTIP_EFFECT.byNameCodec();

    Codec<? extends TooltipEffect> codec();

    boolean canApply(List<TooltipEffect> effects);

    default void renderIcon(GuiGraphics graphics, TooltipContext context, int x, int y) {}

    default void renderBack(GuiGraphics graphics, TooltipContext context, int x, int y, int width, int height) {}

    default void renderFront(GuiGraphics graphics, TooltipContext context, int x, int y, int width, int height) {}

    static void bootstrap(BiConsumer<String, Supplier<Codec<? extends TooltipEffect>>> registrar) {

        registrar.accept("rim_light", () -> RimLightEffect.CODEC);
        registrar.accept("ray_glow", () -> RayGlowEffect.CODEC);
        registrar.accept("inward_particle", () -> InwardParticleEffect.CODEC);
        registrar.accept("icon_particle", () -> IconParticleEffect.CODEC);
    }
}
