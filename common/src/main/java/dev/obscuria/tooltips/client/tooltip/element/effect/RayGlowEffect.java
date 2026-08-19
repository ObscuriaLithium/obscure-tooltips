package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.content.util.color.ARGB;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;

public record RayGlowEffect(
        ARGB primaryColor,
        ARGB secondaryColor
) implements TooltipEffect {

    public static final ResourceLocation TEXTURE;
    public static final MapCodec<RayGlowEffect> CODEC;

    @Override
    public MapCodec<RayGlowEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof RayGlowEffect);
    }

    @Override
    public void renderIcon(TooltipState state, GuiGraphics graphics, int x, int y) {
        final var time = state.timeInSeconds();
        final var base = Mth.clamp(Easing.EASE_OUT_CUBIC.compute(time / 0.5f), 0f, 1f);
        final var scale = base + 0.75f * Math.max(0f, Easing.EASE_IN_CUBIC.mergeOut(Easing.EASE_OUT_CUBIC, 0.35f).compute(time));

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

        GraphicUtils.setShaderColor(primaryColor);
        renderSegment(graphics, x, y, 1f * scale, 0.5f, time);

        GraphicUtils.setShaderColor(primaryColor.lerp(secondaryColor, 0.5f));
        renderSegment(graphics, x, y, 0.75f * scale, -0.33f, time);

        GraphicUtils.setShaderColor(secondaryColor);
        renderSegment(graphics, x, y, 0.5f * scale, 0.25f, time);

        GraphicUtils.resetShaderColor();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void renderSegment(GuiGraphics graphics, int x, int y, float scale, float rotDelta, float timer) {

        graphics.pose().pushPose();
        graphics.pose().translate((float) x, (float) y, 0f);
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().mulPose(Axis.ZP.rotation(rotDelta * 3f + rotDelta * timer));
        graphics.blit(TEXTURE, -32, -32, 0f, 0f, 64, 64, 64, 64);
        graphics.pose().popPose();
    }

    static {
        TEXTURE = ObscureTooltips.key("textures/gui/effect/ray_glow.png");
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                ARGB.CODEC.fieldOf("primary_color").forGetter(RayGlowEffect::primaryColor),
                ARGB.CODEC.fieldOf("secondary_color").forGetter(RayGlowEffect::secondaryColor)
        ).apply(codec, RayGlowEffect::new));
    }
}
