package dev.obscuria.tooltips.client.element.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.tooltips.client.element.QuadPalette;
import dev.obscuria.tooltips.client.particle.GraphicUtils;
import net.minecraft.client.gui.GuiGraphics;

public record ColorRectSlot(
        QuadPalette palette,
        ARGB borders
) implements TooltipSlot {

    public static final Codec<ColorRectSlot> CODEC;

    @Override
    public Codec<ColorRectSlot> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        GraphicUtils.drawRect(graphics, x + 1, y + 1, width - 2, height - 2, palette);
        GraphicUtils.drawHLine(graphics, x + 1, y, width - 2, borders, borders);
        GraphicUtils.drawHLine(graphics, x + 1, y + height - 1, width - 2, borders, borders);
        GraphicUtils.drawVLine(graphics, x, y + 1, height - 2, borders, borders);
        GraphicUtils.drawVLine(graphics, x + width - 1, y + 1, height - 2, borders, borders);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                QuadPalette.CODEC.fieldOf("palette").forGetter(ColorRectSlot::palette),
                ARGB.CODEC.fieldOf("borders").forGetter(ColorRectSlot::borders)
        ).apply(codec, ColorRectSlot::new));
    }
}
