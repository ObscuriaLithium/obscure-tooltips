package dev.obscuria.tooltips.client.element.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.element.Transform;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.client.gui.GuiGraphics;

public record StaticIcon(Transform transform) implements TooltipIcon {

    public static final Codec<StaticIcon> CODEC;

    @Override
    public Codec<StaticIcon> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, TooltipContext context, int x, int y) {
        pushTransform(transform, graphics, context, x, y);
        graphics.renderItem(context.stack(), 0, 0);
        popTransform(graphics);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Transform.CODEC.fieldOf("transform").forGetter(StaticIcon::transform)
        ).apply(codec, StaticIcon::new));
    }
}
