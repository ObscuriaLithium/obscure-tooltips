package dev.obscuria.obscure_tooltips.client.tooltip.element.panel;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;

@Desugar
public record BlankPanel() implements TooltipPanel {

    public static final BlankPanel INSTANCE = new BlankPanel();

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}
