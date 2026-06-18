package dev.obscuria.tooltips.client.tooltip.element.slot;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.tooltips.client.render.GuiGraphics;

@Desugar
public record BlankSlot() implements TooltipSlot {

    public static final BlankSlot INSTANCE = new BlankSlot();

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}
