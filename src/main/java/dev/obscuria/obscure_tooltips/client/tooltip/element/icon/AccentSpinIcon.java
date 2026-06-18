package dev.obscuria.obscure_tooltips.client.tooltip.element.icon;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.element.SoundTemplate;
import dev.obscuria.obscure_tooltips.client.tooltip.element.Transform;
import dev.obscuria.obscure_tooltips.util.easing.Easing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

@Desugar
public record AccentSpinIcon(Transform transform, Optional<SoundTemplate> sound) implements TooltipIcon {

    public static AccentSpinIcon fromJson(JsonObject json) {
        final Transform transform = Transform.fromJson(JsonUtils.getJsonObject(json, "transform"));
        final Optional<SoundTemplate> sound = json.has("sound")
                ? Optional.of(SoundTemplate.fromJson(JsonUtils.getJsonObject(json, "sound")))
                : Optional.empty();
        return new AccentSpinIcon(transform, sound);
    }

    @Override
    public void render(TooltipState state, GuiGraphics graphics, int x, int y) {
        pushTransform(state, transform, graphics, x, y);
        graphics.renderItem(state.stack, 0, 0);
        popTransform(graphics);
        sound.ifPresent(state::maybePlayIconSound);
    }

    @Override
    public void applyScale(TooltipState state, GuiGraphics graphics, int x, int y) {
        final float time = state.timeInSeconds();
        final float scale = (time < 0.25f) ? (Easing.EASE_OUT_CUBIC.compute(time / 0.25f) * 1.33f) : ((time < 0.5f) ? (1.33f - 0.33f * Easing.EASE_OUT_CUBIC.compute((time - 0.25f) / 0.25f)) : 1.0f);
        graphics.pose().scale(scale, scale, scale);
    }

    @Override
    public void applyRotation(TooltipState state, GuiGraphics graphics, int x, int y) {
        final float rotation = 360.0f * MathHelper.clamp(Easing.EASE_OUT_EXPO.compute(state.timeInSeconds()), 0.0f, 1.0f);
        graphics.pose().rotateY(rotation);
    }
}
