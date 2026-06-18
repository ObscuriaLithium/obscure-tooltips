package dev.obscuria.obscure_tooltips.client.component;

import com.github.bsideup.jabel.Desugar;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import net.minecraft.client.gui.FontRenderer;

@Desugar
public record TextComponent(String text) implements TooltipComponent {

    @Override
    public int getWidth(FontRenderer font) {
        return font.getStringWidth(text);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void renderText(FontRenderer font, int x, int y, GuiGraphics graphics) {
        graphics.drawString(font, text, x, y, 0xFFFFFFFF);
    }
}
