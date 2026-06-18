package dev.obscuria.obscure_tooltips.client.tooltip.element.effect;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.TooltipHelper;
import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.obscure_tooltips.config.ARGBProvider;
import dev.obscuria.obscure_tooltips.util.color.ARGB;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.Vec2f;

import java.util.List;
import java.util.function.IntFunction;

@Desugar
public record ShimmerEffect(ARGBProvider innerColor, ARGBProvider accentColor, ARGBProvider outerColor,
                            float frequency, float speed) implements TooltipEffect {

    public static ShimmerEffect fromJson(JsonObject json) {
        return new ShimmerEffect(
                ARGBProvider.fromJson(json.get("inner_color")),
                ARGBProvider.fromJson(json.get("accent_color")),
                ARGBProvider.fromJson(json.get("outer_color")),
                JsonUtils.getFloat(json, "frequency"),
                JsonUtils.getFloat(json, "speed"));
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof ShimmerEffect);
    }

    @Override
    public void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {
        final var actX = x - 3;
        final var actY = y - 3;
        final var actWidth = width + 6;
        final var actHeight = height + 6;

        final var ctrlWidth = actWidth - 24.0F;
        final var ctrlHeight = actHeight - 24.0F;
        final var ctrlLeft = actX + 12.0F;
        final var ctrlTop = actY + 12.0F;
        final var ctrlRight = ctrlLeft + ctrlWidth;
        final var ctrlBottom = ctrlTop + ctrlHeight;
        final var ctrlCenter = new Vec2f(ctrlLeft + ctrlWidth * 0.5F, ctrlTop + ctrlHeight * 0.5F);

        final var hSegments = Math.max(1, (int) (32.0F * ((float) actHeight / (float) actWidth)));
        final var time = state.timeInSeconds();
        
        TooltipHelper.enableGlowingRenderer();
        renderSide(graphics, 32,
                i -> new Vec2f(actX + actWidth * (float) i / 32.0F, actY),
                i -> new Vec2f(actX + actWidth * (float) (i + 1) / 32.0F, actY),
                i -> new Vec2f(ctrlLeft + ctrlWidth * i / 32.0F, ctrlTop),
                i -> new Vec2f(ctrlLeft + ctrlWidth * (i + 1) / 32.0F, ctrlTop), false, ctrlCenter, time);
        renderSide(graphics, 32,
                i -> new Vec2f(actX + actWidth * (float) (i + 1) / 32.0F, actY + actHeight),
                i -> new Vec2f(actX + actWidth * (float) i / 32.0F, actY + actHeight),
                i -> new Vec2f(ctrlLeft + ctrlWidth * (i + 1) / 32.0F, ctrlBottom),
                i -> new Vec2f(ctrlLeft + ctrlWidth * i / 32.0F, ctrlBottom), false, ctrlCenter, time);
        renderSide(graphics, hSegments,
                i -> new Vec2f(actX, actY + actHeight * (float) i / (float) hSegments),
                i -> new Vec2f(actX, actY + actHeight * (float) (i + 1) / (float) hSegments),
                i -> new Vec2f(ctrlLeft, ctrlTop + ctrlHeight * i / hSegments),
                i -> new Vec2f(ctrlLeft, ctrlTop + ctrlHeight * (i + 1) / hSegments), true, ctrlCenter, time);
        renderSide(graphics, hSegments,
                i -> new Vec2f(actX + actWidth, actY + actHeight * (float) (i + 1) / (float) hSegments),
                i -> new Vec2f(actX + actWidth, actY + actHeight * (float) i / (float) hSegments),
                i -> new Vec2f(ctrlRight, ctrlTop + ctrlHeight * (i + 1) / hSegments),
                i -> new Vec2f(ctrlRight, ctrlTop + ctrlHeight * i / hSegments), true, ctrlCenter, time);
        TooltipHelper.disableGlowingRenderer();
    }

    private void renderSide(GuiGraphics graphics, int segments, IntFunction<Vec2f> inner1Func, IntFunction<Vec2f> inner2Func,
                            IntFunction<Vec2f> ctrl1Func, IntFunction<Vec2f> ctrl2Func, boolean flip, Vec2f ctrlCenter, float time) {
        for (int i = 0; i < segments; i++) {
            final var inner1 = inner1Func.apply(i);
            final var inner2 = inner2Func.apply(i);
            final var ctrl1 = ctrl1Func.apply(i);
            final var ctrl2 = ctrl2Func.apply(i);

            final var d1 = new Vec2f(inner1.x - ctrlCenter.x, inner1.y - ctrlCenter.y);
            final var d2 = new Vec2f(inner2.x - ctrlCenter.x, inner2.y - ctrlCenter.y);
            final var a1 = (float) -Math.atan2(d1.y, d1.x);
            final var a2 = (float) -Math.atan2(d2.y, d2.x);
            final var t1 = 0.5F + 0.5F * (float) Math.cos(a1 * frequency + time * speed);
            final var t2 = 0.5F + 0.5F * (float) Math.cos(a2 * frequency + time * speed);

            final var outer1 = lerp(inner1, ctrl1, 0.3F + 0.2F * t1);
            final var outer2 = lerp(inner2, ctrl2, 0.3F + 0.2F * t2);
            final var color1 = innerColor.get().lerp(accentColor.get(), t1);
            final var color2 = innerColor.get().lerp(accentColor.get(), t2);

            if (flip) {
                GraphicUtils.drawQuad(graphics, inner1.x, inner1.y, inner2.x, inner2.y, outer2.x, outer2.y, outer1.x, outer1.y,
                        color1, color2, outerColor.get(), outerColor.get());
            } else {
                GraphicUtils.drawQuad(graphics, inner2.x, inner2.y, inner1.x, inner1.y, outer1.x, outer1.y, outer2.x, outer2.y,
                        color2, color1, outerColor.get(), outerColor.get());
            }
        }
    }

    private static Vec2f lerp(Vec2f a, Vec2f b, float t) {
        return new Vec2f(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t);
    }
}
