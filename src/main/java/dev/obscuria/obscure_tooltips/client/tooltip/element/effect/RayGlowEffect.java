package dev.obscuria.obscure_tooltips.client.tooltip.element.effect;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.config.ARGBProvider;
import dev.obscuria.obscure_tooltips.util.color.ARGB;
import dev.obscuria.obscure_tooltips.util.easing.Easing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Desugar
public record RayGlowEffect(ARGBProvider primaryColor, ARGBProvider secondaryColor) implements TooltipEffect {

    public static final ResourceLocation TEXTURE = ObscureTooltips.resource("textures/gui/effect/ray_glow.png");

    public static RayGlowEffect fromJson(JsonObject json) {
        return new RayGlowEffect(
                ARGBProvider.fromJson(json.get("primary_color")),
                ARGBProvider.fromJson(json.get("secondary_color")));
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof RayGlowEffect);
    }

    @Override
    public void renderIcon(TooltipState state, GuiGraphics graphics, int x, int y) {
        final var time = state.timeInSeconds();
        final var base = MathHelper.clamp(Easing.EASE_OUT_CUBIC.compute(time / 0.5f), 0f, 1f);
        final var scale = base + 0.75f * Math.max(0f, Easing.EASE_IN_CUBIC.mergeOut(Easing.EASE_OUT_CUBIC, 0.35f).compute(time));
        
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        renderSegment(graphics, x, y, 1.0f * scale, 0.5f, time, primaryColor.get());
        renderSegment(graphics, x, y, 0.75f * scale, -0.33f, time, primaryColor.get().lerp(secondaryColor.get(), 0.5f));
        renderSegment(graphics, x, y, 0.5f * scale, 0.25f, time, secondaryColor.get());
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableBlend();
    }

    private void renderSegment(final GuiGraphics graphics, final int x, final int y, final float scale, final float rotDelta, final float timer, ARGB argb) {
        graphics.pose().pushMatrix();
        graphics.pose().translate((float) x, (float) y, 0.0f);
        graphics.pose().scale(scale, scale, scale);
        graphics.pose().rotateRadiansZ(rotDelta * 3.0f + rotDelta * timer);
        graphics.blitGlow(RayGlowEffect.TEXTURE, -32, -32, 0.0f, 0.0f, 64, 64, 64, 64, argb);
        graphics.pose().popMatrix();
    }
}
