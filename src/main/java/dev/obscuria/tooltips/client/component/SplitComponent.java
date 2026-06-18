package dev.obscuria.tooltips.client.component;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.tooltips.client.TooltipHelper;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import net.minecraft.client.gui.FontRenderer;

import java.util.List;

@Desugar
public record SplitComponent(
        TooltipComponent left,
        List<TooltipComponent> right
) implements TooltipComponent {

    @Override
    public int getHeight() {
        return Math.max(left.getHeight(), TooltipHelper.heightOf(right));
    }

    @Override
    public int getWidth(FontRenderer font) {
        return left.getWidth(font) + 3 + TooltipHelper.widthOf(right, font);
    }

    @Override
    public void renderText(FontRenderer font, int x, int y, GuiGraphics graphics) {
        var componentX = x + left.getWidth(font) + 3;
        var componentY = y;
        for (var component : right) {
            component.renderText(font, componentX, componentY, graphics);
            componentY += component.getHeight();
        }

        left.renderText(font, x, y, graphics);
    }

    @Override
    public void renderImage(FontRenderer font, int x, int y, GuiGraphics graphics) {
        var componentX = x + left.getWidth(font) + 3;
        var componentY = y;
        for (var component : right) {
            component.renderImage(font, componentX, componentY, graphics);
            componentY += component.getHeight();
        }

        left.renderImage(font, x, y, graphics);
    }
}
