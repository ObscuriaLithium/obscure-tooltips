package dev.obscuria.tooltips.client.element.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.tooltips.client.element.QuadPalette;
import dev.obscuria.tooltips.client.particle.GraphicUtils;
import dev.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

import java.util.List;

public record RimLightEffect(
        QuadPalette outerPalette,
        QuadPalette innerPalette
) implements TooltipEffect {

    public static final Codec<RimLightEffect> CODEC;

    @Override
    public Codec<RimLightEffect> codec() {
        return CODEC;
    }

    @Override
    public boolean canApply(List<TooltipEffect> effects) {
        return effects.stream().noneMatch(it -> it instanceof RimLightEffect);
    }

    @Override
    public void renderBack(GuiGraphics graphics, TooltipContext context, int x, int y, int width, int height) {

        final var pX = x - 3f;
        final var pY = y - 3f;
        final var pWidth = width + 6f;
        final var pHeight = height + 6f;

        final var matrix = graphics.pose().last().pose();
        final var buffer = graphics.bufferSource().getBuffer(RenderType.guiOverlay());
        final var scale = 0.8f + 0.4f * (float) Math.cos(context.timeInSeconds());
        final var offset = Math.min(pWidth, pHeight) * 0.25f * scale;

        GraphicUtils.color(buffer.vertex(matrix, pX, pY, 0f), outerPalette.topLeft()).endVertex();
        GraphicUtils.color(buffer.vertex(matrix, pX + offset, pY + offset, 0f), innerPalette.topLeft()).endVertex();
        GraphicUtils.color(buffer.vertex(matrix, pX + pWidth - offset, pY + offset, 0f), innerPalette.topRight()).endVertex();
        GraphicUtils.color(buffer.vertex(matrix, pX + pWidth, pY, 0f), outerPalette.topRight()).endVertex();

        GraphicUtils.color(buffer.vertex(matrix, pX + offset, pY + pHeight - offset, 0f), innerPalette.bottomLeft()).endVertex();
        GraphicUtils.color(buffer.vertex(matrix, pX, pY + pHeight, 0f), outerPalette.bottomLeft()).endVertex();
        GraphicUtils.color( buffer.vertex(matrix, pX + pWidth, pY + pHeight, 0f), outerPalette.bottomRight()).endVertex();
        GraphicUtils.color(buffer.vertex(matrix, pX + pWidth - offset, pY + pHeight - offset, 0f), innerPalette.bottomRight()).endVertex();

        GraphicUtils.color(buffer.vertex(matrix, pX, pY, 0f), outerPalette.topLeft()).endVertex();
        GraphicUtils.color( buffer.vertex(matrix, pX, pY + pHeight, 0f), outerPalette.bottomLeft()).endVertex();
        GraphicUtils.color( buffer.vertex(matrix, pX + offset, pY + pHeight - offset, 0f), innerPalette.bottomLeft()).endVertex();
        GraphicUtils.color( buffer.vertex(matrix, pX + offset, pY + offset, 0f), innerPalette.topLeft()).endVertex();

        GraphicUtils.color( buffer.vertex(matrix, pX + pWidth - offset, pY + offset, 0f), innerPalette.topRight()).endVertex();
        GraphicUtils.color(  buffer.vertex(matrix, pX + pWidth - offset, pY + pHeight - offset, 0f), innerPalette.bottomRight()).endVertex();
        GraphicUtils.color( buffer.vertex(matrix, pX + pWidth, pY + pHeight, 0f), outerPalette.bottomRight()).endVertex();
        GraphicUtils.color( buffer.vertex(matrix, pX + pWidth, pY, 0f), outerPalette.topRight()).endVertex();
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                QuadPalette.CODEC.fieldOf("outer_palette").forGetter(RimLightEffect::outerPalette),
                QuadPalette.CODEC.fieldOf("inner_palette").forGetter(RimLightEffect::innerPalette)
        ).apply(codec, RimLightEffect::new));
    }
}
