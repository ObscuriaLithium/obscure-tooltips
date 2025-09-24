package dev.obscuria.tooltips.client.element.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.easing.Easing;
import dev.obscuria.tooltips.client.element.Transform;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.client.gui.GuiGraphics;

public record AccentIcon(Transform transform) implements TooltipIcon {

    public static final Codec<AccentIcon> CODEC;

    @Override
    public Codec<AccentIcon> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, TooltipContext context, int x, int y) {
        pushTransform(transform, graphics, context, x, y);
        graphics.renderItem(context.stack(), 0, 0);
        popTransform(graphics);
    }

    @Override
    public void applyScale(GuiGraphics graphics, TooltipContext context, int x, int y) {
        final var time = context.timeInSeconds();
        final var scale = (time < 0.25f)
                ? Easing.EASE_OUT_CUBIC.compute(time / 0.25f) * 1.25f
                : (time < 0.5f)
                ? 1.25f - 0.25f * Easing.EASE_OUT_CUBIC.compute((time - 0.25f) / 0.25f)
                : 1f;
        graphics.pose().scale(scale, scale, scale);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Transform.CODEC.fieldOf("transform").forGetter(AccentIcon::transform)
        ).apply(codec, AccentIcon::new));
    }
}
