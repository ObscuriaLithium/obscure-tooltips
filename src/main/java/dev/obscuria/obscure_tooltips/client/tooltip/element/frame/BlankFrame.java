package dev.obscuria.obscure_tooltips.client.tooltip.element.frame;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;

@Desugar
public record BlankFrame() implements TooltipFrame {

    public static final BlankFrame INSTANCE = new BlankFrame();

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}
