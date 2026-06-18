package dev.obscuria.tooltips.client.component;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.tooltips.config.ClientConfig;
import dev.obscuria.tooltips.util.color.ARGB;
import net.minecraft.client.gui.FontRenderer;

@Desugar
public record HeaderComponent(
        TooltipState state,
        TooltipComponent title,
        TooltipComponent label,
        boolean drawSeparator,
        ARGB separatorColor
) implements TooltipComponent {
    @Override
    public int getHeight() {
        return drawSeparator ? 25 : 22;
    }

    @Override
    public int getWidth(FontRenderer font) {
        return 22 + Math.max(title.getWidth(font), label.getWidth(font));
    }

    @Override
    public void renderText(FontRenderer font, int x, int y, GuiGraphics graphics) {
        if (!ClientConfig.LABELS_ENABLED.get() || label instanceof BlankComponent) {
            title.renderText(font, 22 + x, 1 + y + 5, graphics);
        } else {
            title.renderText(font, 22 + x, 1 + y, graphics);
            label.renderText(font, 22 + x, 1 + y + title.getHeight(), graphics);
        }
    }

    @Override
    public void renderImage(FontRenderer font, int x, int y, GuiGraphics graphics) {
        state.style.slot().ifPresent(it -> it.render(graphics, x, y, 20, 20));
        state.style.effects().forEach(it -> it.renderIcon(state, graphics, x + 10, y + 10));
        state.style.icon().ifPresent(it -> it.render(state, graphics, x + 10, y + 10));

        if (!drawSeparator) return;
        final var length = getWidth(font) / 2;
        final var edgeColor = separatorColor.withAlpha(0f);
        GraphicUtils.drawHLine(graphics, x, y + 22, length, edgeColor, separatorColor);
        GraphicUtils.drawHLine(graphics, x + length, y + 22, 1 + length, separatorColor, edgeColor);
    }
}
