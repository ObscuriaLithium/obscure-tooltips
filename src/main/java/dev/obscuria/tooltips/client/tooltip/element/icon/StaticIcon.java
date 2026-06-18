package dev.obscuria.tooltips.client.tooltip.element.icon;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.element.SoundTemplate;
import dev.obscuria.tooltips.client.tooltip.element.Transform;
import net.minecraft.util.JsonUtils;

import java.util.Optional;

@Desugar
public record StaticIcon(Transform transform, Optional<SoundTemplate> sound) implements TooltipIcon {

    public static StaticIcon fromJson(JsonObject json) {
        final Transform transform = Transform.fromJson(JsonUtils.getJsonObject(json, "transform"));
        final Optional<SoundTemplate> sound = json.has("sound")
                ? Optional.of(SoundTemplate.fromJson(JsonUtils.getJsonObject(json, "sound")))
                : Optional.empty();
        return new StaticIcon(transform, sound);
    }

    @Override
    public void render(TooltipState state, GuiGraphics graphics, int x, int y) {
        pushTransform(state, transform, graphics, x, y);
        graphics.renderItem(state.stack, 0, 0);
        popTransform(graphics);
        sound.ifPresent(state::maybePlayIconSound);
    }
}
