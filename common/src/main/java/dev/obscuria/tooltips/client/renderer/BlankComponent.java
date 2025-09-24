package dev.obscuria.tooltips.client.renderer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public record BlankComponent() implements ClientTooltipComponent {

    public static final BlankComponent INSTANCE = new BlankComponent();

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth(Font font) {
        return 0;
    }
}
