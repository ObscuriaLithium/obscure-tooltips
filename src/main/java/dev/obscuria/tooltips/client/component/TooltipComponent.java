package dev.obscuria.tooltips.client.component;

import dev.obscuria.tooltips.client.render.GuiGraphics;
import net.minecraft.client.gui.FontRenderer;

public interface TooltipComponent {
    int getWidth(FontRenderer font);

    int getHeight();

    default void renderText(FontRenderer font, int x, int y, GuiGraphics graphics) {}

    default void renderImage(FontRenderer font, int x, int y, GuiGraphics graphics) {}

    static TooltipComponent create(String line) {
        return new TextComponent(line);
    }
}
