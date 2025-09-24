package dev.obscuria.tooltips.client.element.frame;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public record NineSlicedFrame(ResourceLocation textureSheet) implements TooltipFrame {

    public static final Codec<NineSlicedFrame> CODEC;

    @Override
    public Codec<NineSlicedFrame> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.blit(textureSheet, x - 31, y - 31, 1f, 1f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x + width - 14, y - 31, 93f, 1f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x - 31, y + height - 14, 1f, 93f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x + width - 14, y + height - 14, 93f, 93f, 45, 45, 140, 140);

        graphics.blit(textureSheet, x + width / 2 - 23, y - 31, 47f, 1f, 45, 45, 140, 140);
        graphics.blit(textureSheet, x + width / 2 - 23, y + height - 14, 47f, 93f, 45, 45, 140, 140);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ResourceLocation.CODEC.fieldOf("texture_sheet").forGetter(NineSlicedFrame::textureSheet)
        ).apply(codec, NineSlicedFrame::new));
    }
}
