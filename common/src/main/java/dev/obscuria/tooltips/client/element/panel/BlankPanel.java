package dev.obscuria.tooltips.client.element.panel;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;

public record BlankPanel() implements TooltipPanel {

    public static final BlankPanel INSTANCE = new BlankPanel();
    public static final Codec<BlankPanel> CODEC = Codec.unit(INSTANCE);

    @Override
    public Codec<BlankPanel> codec() {
        return CODEC;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height) {}
}
