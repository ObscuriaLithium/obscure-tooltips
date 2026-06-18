package dev.obscuria.tooltips.client.component;

import net.minecraft.client.gui.FontRenderer;

public final class BlankComponent implements TooltipComponent {

    public static final BlankComponent INSTANCE = new BlankComponent();

    @Override
    public int getWidth(FontRenderer font) {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
